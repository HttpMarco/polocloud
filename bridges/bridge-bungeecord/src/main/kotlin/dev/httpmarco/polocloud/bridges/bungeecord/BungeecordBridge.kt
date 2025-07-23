package dev.httpmarco.polocloud.bridges.bungeecord

import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.plugin.Plugin
import java.net.InetSocketAddress

class BungeecordBridge : Plugin() {

    override fun onEnable() {
        ProxyServer.getInstance().servers.clear()

        // register a simple fallback dummy for reconnect handler
        ProxyServer.getInstance().servers["fallback"] =
            ProxyServer.getInstance().constructServerInfo("fallback", InetSocketAddress("127.0.0.1", 0), null, false)

        val bridgeInstance = BungeecordBridgeInstance()
        ProxyServer.getInstance().reconnectHandler = BungeecordReconnectHandler(bridgeInstance)
        ProxyServer.getInstance().pluginManager.registerListener(this, bridgeInstance)


    }
}