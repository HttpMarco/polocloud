package dev.httpmarco.polocloud.bridges.bungeecord

import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.plugin.Plugin

class BungeecordBridge : Plugin() {

    override fun onEnable() {
        ProxyServer.getInstance().servers.clear()

        val bridgeInstance = BungeecordBridgeInstance()
        ProxyServer.getInstance().reconnectHandler = BungeecordReconnectHandler(bridgeInstance)
        ProxyServer.getInstance().pluginManager.registerListener(this, bridgeInstance)
    }
}