package dev.httpmarco.polocloud.bridges.bungeecord

import dev.httpmarco.polocloud.bridge.api.BridgeInstance
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.event.PreLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import java.net.InetSocketAddress

class BungeecordBridgeInstance : BridgeInstance<ServerInfo>(), Listener {

    val registeredFallbacks = ArrayList<ServerInfo>()

    init {
        this.initialize()
    }

    override fun unregisterService(identifier: ServerInfo) {
        ProxyServer.getInstance().servers.remove(identifier.name)
    }

    override fun findInfo(name: String): ServerInfo? {
        return ProxyServer.getInstance().getServerInfo(name)
    }

    override fun generateInfo(name: String, hostname: String, port: Int): ServerInfo {
        return ProxyServer.getInstance().constructServerInfo(name, InetSocketAddress(hostname, port), null, false)
    }

    override fun registerService(identifier: ServerInfo, fallback: Boolean) {
        ProxyServer.getInstance().servers[identifier.name] = identifier

        if (fallback) {
            registeredFallbacks.add(identifier)
        }
    }

    @EventHandler
    fun onPostLogin(event: PreLoginEvent) {
        if(registeredFallbacks.isEmpty()) {
            event.reason = TextComponent("No fallback servers are registered.")
        }
    }
}