package dev.httpmarco.polocloud.addons.signs.bukkit

import org.bukkit.plugin.java.JavaPlugin

class BukkitBootstrap : JavaPlugin() {

    override fun onEnable() {
        // init connectors and configuration files
        BukkitConnectors

        getCommand("signs")!!.setExecutor(BukkitSignCommand())
    }
}