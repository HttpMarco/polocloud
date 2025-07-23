package dev.httpmarco.polocloud.bridges.bungeecord

import net.md_5.bungee.api.ReconnectHandler
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.ProxiedPlayer

class BungeecordReconnectHandler(private val bridge: BungeecordBridgeInstance) : ReconnectHandler {

    override fun getServer(player: ProxiedPlayer?): ServerInfo? {
        return bridge.registeredFallbacks.minByOrNull { it.players.size }
    }

    override fun setServer(player: ProxiedPlayer?) {
        TODO("Not yet implemented")
    }

    override fun save() {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }


}