package dev.httpmarco.polocloud.bridges.waterdog

import dev.httpmarco.polocloud.bridge.api.BridgeInstance
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.events.definitions.PlayerJoinEvent
import dev.httpmarco.polocloud.shared.events.definitions.PlayerLeaveEvent
import dev.httpmarco.polocloud.shared.player.PolocloudPlayer
import dev.waterdog.waterdogpe.ProxyServer
import dev.waterdog.waterdogpe.event.defaults.PlayerDisconnectedEvent
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent
import dev.waterdog.waterdogpe.event.defaults.ServerConnectedEvent
import dev.waterdog.waterdogpe.network.serverinfo.BedrockServerInfo
import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo
import java.net.InetSocketAddress

class WaterdogBridgeInstance : BridgeInstance<ServerInfo>() {

    val registeredFallbacks = ArrayList<ServerInfo>()

    init {
        registerEvents()
        initialize()
    }

    override fun generateInfo(
        name: String,
        hostname: String,
        port: Int
    ): ServerInfo {
        val serverInfo = BedrockServerInfo(name, InetSocketAddress(hostname, port), null)
        ProxyServer.getInstance().registerServerInfo(serverInfo)

        return serverInfo
    }

    override fun registerService(
        identifier: ServerInfo,
        fallback: Boolean
    ) {
        ProxyServer.getInstance().registerServerInfo(identifier)

        if (fallback) {
            registeredFallbacks.add(identifier)
        }
    }

    override fun unregisterService(identifier: ServerInfo) {
        ProxyServer.getInstance().removeServerInfo(identifier.serverName)
        registeredFallbacks.remove(identifier)
    }

    override fun findInfo(name: String): ServerInfo? {
        return ProxyServer.getInstance().getServerInfo(name)
    }

    private fun registerEvents() {
        val eventManager = ProxyServer.getInstance().eventManager

        eventManager.subscribe(PlayerLoginEvent::class.java) { event ->
            if (registeredFallbacks.isEmpty()) {
                event.cancelReason = "No fallback servers are registered."
                event.isCancelled = true
            }
        }

        eventManager.subscribe(ServerConnectedEvent::class.java) { event ->
            val polocloudPlayer = Polocloud.instance().playerProvider().findByName(event.player.name)
            if (polocloudPlayer == null) {
                val fallback = findFallback()
                if (fallback != null) {
                    event.player.connect(fallback)
                } else {
                    event.player.disconnect("No fallback servers available.")
                }
            }

            val player = event.player
            val cloudPlayer = PolocloudPlayer(player.name, player.uniqueId, event.targetServer.serverName)
            updatePolocloudPlayer(PlayerJoinEvent(cloudPlayer))
        }

        eventManager.subscribe(PlayerDisconnectedEvent::class.java) { event ->
            val player = event.player
            val serverName = player.connectingServer?.serverName ?: "unknown"
            val cloudPlayer = PolocloudPlayer(player.name, player.uniqueId, serverName)
            updatePolocloudPlayer(PlayerLeaveEvent(cloudPlayer))
        }
    }

    fun findFallback(): ServerInfo? {
        return registeredFallbacks.minByOrNull { it.players.size }
    }
}