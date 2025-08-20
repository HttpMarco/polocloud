package dev.httpmarco.polocloud.bridges.waterdog

import dev.waterdog.waterdogpe.network.connection.handler.DefaultReconnectHandler
import dev.waterdog.waterdogpe.network.connection.handler.ReconnectReason
import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo
import dev.waterdog.waterdogpe.player.ProxiedPlayer

class WaterdogReconnectHandler(private val bridge: WaterdogBridgeInstance) : DefaultReconnectHandler() {

    override fun getFallbackServer(
        player: ProxiedPlayer?,
        oldServer: ServerInfo?,
        reason: ReconnectReason?,
        kickMessage: String?
    ): ServerInfo? {
        return bridge.findFallback()
    }
}