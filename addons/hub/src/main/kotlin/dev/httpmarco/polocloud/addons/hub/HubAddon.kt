package dev.httpmarco.polocloud.addons.hub

import dev.httpmarco.polocloud.addons.api.ConfigFactory
import java.io.File

class HubAddon(dataFolder: File, minimessagePrefix: Boolean) {
    private val configFactory = ConfigFactory(HubConfiguration::class.java, dataFolder, "hub-config.json")

    init {
        changePrefix(minimessagePrefix)
    }

    val config: HubConfigAccessor = HubConfigAccessor(configFactory.config)


    fun changePrefix(minimessagePrefix: Boolean) {
        if (!this.configFactory.config.prefix.isEmpty()) {
            return
        }

        if (minimessagePrefix) {
            this.configFactory.config.prefix = "<gradient:#00fdee:#118bd1><bold>PoloCloud</bold></gradient> <dark_gray>» <gray>"
            this.configFactory.save()
            return
        }

        this.configFactory.config.prefix = "§b§lPoloCloud §8» §7"
        this.configFactory.save()
    }
}