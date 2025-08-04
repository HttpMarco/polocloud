package dev.httpmarco.polocloud.addons.signs.platform.spigot

import dev.httpmarco.polocloud.addons.signs.Connectors
import org.bukkit.plugin.java.JavaPlugin

class SpigotConnectorBootstrap : JavaPlugin() {

    override fun onEnable() {
        Connectors()
    }

    override fun onDisable() {

    }
}