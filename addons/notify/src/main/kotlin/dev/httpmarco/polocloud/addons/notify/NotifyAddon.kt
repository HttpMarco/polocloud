package dev.httpmarco.polocloud.addons.notify

import dev.httpmarco.polocloud.addons.api.ConfigFactory
import dev.httpmarco.polocloud.addons.api.LegacyFormatter
import dev.httpmarco.polocloud.addons.api.MessageFormatter
import dev.httpmarco.polocloud.addons.api.MiniMessageFormatter
import java.io.File

class NotifyAddon(dataFolder: File, minimessage: Boolean) {
    private val configFactory = ConfigFactory(NotifyConfiguration::class.java, dataFolder, "notify-config.json")
    private val formatter: MessageFormatter = if (minimessage) MiniMessageFormatter else LegacyFormatter
    val config: NotifyConfigAccessor = NotifyConfigAccessor(configFactory.config)

    init {
        applyFormatting()
    }

    private fun applyFormatting() {
        val config = this.configFactory.config

        if (config.prefix.isEmpty()) {
            config.prefix = formatter.formatPrefix()
        }

        config.messages.replaceAll { _, value -> formatter.format(value) }

        this.configFactory.save()
    }
}