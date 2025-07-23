package dev.httpmarco.polocloud.addons.signs.platform.spigot

import org.bukkit.plugin.java.JavaPlugin

class SpigotPlugin : JavaPlugin() {

    override fun onEnable() {
        SpigotConnectorDefinition()
    }
}