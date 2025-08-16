package dev.httpmarco.polocloud.addons.proxy

import dev.httpmarco.polocloud.addons.api.ConfigFactory
import dev.httpmarco.polocloud.addons.api.LegacyFormatter
import dev.httpmarco.polocloud.addons.api.MessageFormatter
import dev.httpmarco.polocloud.addons.api.MiniMessageFormatter
import dev.httpmarco.polocloud.sdk.java.Polocloud
import java.io.File

class ProxyAddon(dataFolder: File, minimessage: Boolean) {
    private val configFactory = ConfigFactory(ProxyConfiguration::class.java, dataFolder, "proxy-config.json")
    private val formatter: MessageFormatter = if (minimessage) MiniMessageFormatter else LegacyFormatter
    val config: ProxyConfigAccessor = ProxyConfigAccessor(configFactory.config)
    var poloService = Polocloud.instance().serviceProvider().find(System.getenv("service-name"))!!

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

        if(config.motd.lineOne.isEmpty()) {
            config.motd.lineOne = formatter.formatMotdLineOne()
        }

        if(config.motd.lineTwo.isEmpty()) {
            config.motd.lineTwo = formatter.formatMotdLineTwo()
        }

        if(config.maintenanceMotd.lineOne.isEmpty()) {
            config.maintenanceMotd.lineOne = formatter.formatMaintenanceMotdLineOne()
        }

        if(config.maintenanceMotd.lineTwo.isEmpty()) {
            config.maintenanceMotd.lineTwo = formatter.formatMaintenanceMotdLineTwo()
        }

        if(config.maintenanceMotd.pingMessage.isEmpty()) {
            config.maintenanceMotd.pingMessage = formatter.formatMaintenancePingMessage()
        }

        this.configFactory.save()
    }
}