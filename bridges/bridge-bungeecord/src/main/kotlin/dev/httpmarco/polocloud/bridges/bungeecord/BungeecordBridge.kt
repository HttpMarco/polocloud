package dev.httpmarco.polocloud.bridges.bungeecord

import dev.httpmarco.polocloud.bridge.api.BridgeInstance
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.config.ServerInfo
import java.net.InetSocketAddress

class BungeecordBridge : BridgeInstance<ServerInfo>() {

    override fun generateInfo(
        name: String,
        hostname: String,
        port: Int
    ): ServerInfo {
        return ProxyServer.getInstance().constructServerInfo(
            name,
            InetSocketAddress(hostname, port),
            null,
            false
        )
    }

    override fun registerService(identifier: ServerInfo, fallback: Boolean) {
        ProxyServer.getInstance().servers[identifier.name] = identifier
    }

    override fun unregisterService(identifier: ServerInfo) {
        ProxyServer.getInstance().servers.remove(identifier.name)
    }

    override fun findInfo(name: String): ServerInfo? {
        return ProxyServer.getInstance().getServerInfo(name)
    }
}