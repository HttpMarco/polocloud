package dev.httpmarco.polocloud.addons.signs.bukkit

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class BukkitBootstrap : JavaPlugin() {

    companion object {
        lateinit var plugin: JavaPlugin
    }

    init {
        plugin = this
    }

    override fun onEnable() {
        // init connectors and configuration files
        BukkitConnectors

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord")
        Bukkit.getPluginManager().registerEvents(BukkitConnectorEvents(), this)

        getCommand("signs")!!.setExecutor(BukkitSignCommand())
    }
}