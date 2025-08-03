package dev.httpmarco.polocloud.addons.hub

import dev.httpmarco.polocloud.addons.api.ConfigFactory
import dev.httpmarco.polocloud.addons.api.LegacyFormatter
import dev.httpmarco.polocloud.addons.api.MessageFormatter
import dev.httpmarco.polocloud.addons.api.MiniMessageFormatter
import java.io.File

class HubAddon(dataFolder: File, minimessage: Boolean) {
    private val configFactory = ConfigFactory(HubConfiguration::class.java, dataFolder, "hub-config.json")
    private val formatter: MessageFormatter = if (minimessage) MiniMessageFormatter else LegacyFormatter
    val config: HubConfigAccessor = HubConfigAccessor(configFactory.config)

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