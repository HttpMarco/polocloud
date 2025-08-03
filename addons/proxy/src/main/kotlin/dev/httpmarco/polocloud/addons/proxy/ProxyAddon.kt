package dev.httpmarco.polocloud.addons.proxy

import dev.httpmarco.polocloud.addons.api.ConfigFactory
import dev.httpmarco.polocloud.addons.api.LegacyFormatter
import dev.httpmarco.polocloud.addons.api.MessageFormatter
import dev.httpmarco.polocloud.addons.api.MiniMessageFormatter
import java.io.File

class ProxyAddon(dataFolder: File, minimessage: Boolean) {
    private val configFactory = ConfigFactory(ProxyConfiguration::class.java, dataFolder, "proxy-config.json")
    private val formatter: MessageFormatter = if (minimessage) MiniMessageFormatter else LegacyFormatter
    val config: ProxyConfigAccessor = ProxyConfigAccessor(configFactory.config)

    init {
        applyFormatting()
    }

    private fun applyFormatting() {
        val config = this.configFactory.config

        if (config.prefix.isEmpty()) {
            config.prefix = formatter.formatPrefix()
        }

        config.messages.replaceAll { _, value -> formatter.format(value) }

        if (config.tablist.header.isEmpty()) {
            config.tablist.header = formatter.formatTablistHeader()
        }

        if (config.tablist.footer.isEmpty()) {
            config.tablist.footer = formatter.formatTablistFooter()
        }

        this.configFactory.save()
    }
}