package dev.httpmarco.polocloud.addons.signs.platform.spigot

import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin

class SpigotPlugin : JavaPlugin() {

    override fun onEnable() {
        try {
            val metrics = Metrics(this, 26765) 
            logger.info("bStats Metrics successfully initialized with plugin id 26754")
        } catch (ex: Exception) {
            logger.severe("Failed to initialize bStats Metrics!")
            ex.printStackTrace()
        }

        SpigotConnectorDefinition()
    }
}
