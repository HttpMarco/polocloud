package dev.httpmarco.polocloud.addons.signs.bukkit

import org.bukkit.plugin.java.JavaPlugin

class BukkitBootstrap : JavaPlugin() {

    override fun onEnable() {
        getCommand("signs")!!.setExecutor(BukkitSignCommand())
    }
}