package dev.httpmarco.polocloud.bridges.bungeecord

import net.md_5.bungee.api.ReconnectHandler
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.ProxiedPlayer

class BungeecordReconnectHandler(private val bridge: BungeecordBridgeInstance) : ReconnectHandler {

    override fun getServer(player: ProxiedPlayer?): ServerInfo? {
        return bridge.findFallback()
    }

    override fun setServer(player: ProxiedPlayer?) {}

    override fun save() {}

    override fun close() {}
}