package dev.httpmarco.polocloud.addons.proxy.platform.bungeecord

import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.events.BungeecordMotdUpdater
import dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.events.PlayerLoginEvent
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.plugin.Plugin
import java.io.File

private lateinit var proxyAddon: ProxyAddon

class BungeecordPlatform: Plugin() {
    override fun onEnable() {
        proxyAddon = ProxyAddon(File("plugins/polocloud"), false)
        val config = proxyAddon.config

        val pluginManager = ProxyServer.getInstance().pluginManager
        pluginManager.registerCommand(
            this,
            BungeecordCloudCommand(proxyAddon)
        )

        pluginManager.registerListener(this, BungeecordMotdUpdater(this, config))
        pluginManager.registerListener(this, PlayerLoginEvent(this, config))

        BungeecordTablistUpdater(this, config)

    }

    fun proxyAddon(): ProxyAddon {
        return proxyAddon
    }
}