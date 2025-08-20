package dev.httpmarco.polocloud.bridges.bungeecord

import dev.httpmarco.polocloud.bridge.api.BridgeInstance
import dev.httpmarco.polocloud.shared.events.definitions.PlayerJoinEvent
import dev.httpmarco.polocloud.shared.events.definitions.PlayerLeaveEvent
import dev.httpmarco.polocloud.shared.player.PolocloudPlayer
import dev.httpmarco.polocloud.shared.service.Service
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PreLoginEvent
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.event.ServerConnectedEvent
import net.md_5.bungee.api.event.ServerKickEvent
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
        registeredFallbacks.remove(identifier)
    }

    override fun findInfo(name: String): ServerInfo? {
        return ProxyServer.getInstance().getServerInfo(name)
    }

    override fun generateInfo(service: Service): ServerInfo {
        return ProxyServer.getInstance()
            .constructServerInfo(service.name(), InetSocketAddress(service.hostname, service.port), null, false)
    }

    override fun registerService(identifier: ServerInfo, fallback: Boolean) {
        ProxyServer.getInstance().servers[identifier.name] = identifier

        if (fallback) {
            registeredFallbacks.add(identifier)
        }
    }

    @EventHandler
    fun onPostLogin(event: PreLoginEvent) {
        if (registeredFallbacks.isEmpty()) {
            event.reason = TextComponent("No fallback servers are registered.")
        }
    }

    @EventHandler
    fun connect(event: ServerConnectEvent) {
        if (event.reason == ServerConnectEvent.Reason.JOIN_PROXY || event.reason == ServerConnectEvent.Reason.LOBBY_FALLBACK) {
            val target = registeredFallbacks.minByOrNull { it.players.size }

            if (target == null) {
                event.player.disconnect(TextComponent("No fallback servers available."))
                return
            }

            event.target = target
        }
    }

    @EventHandler
    fun onServerConnected(event: ServerConnectedEvent) {
        val player = event.player
        val serverInfo = event.server.info
        val serviceName = serverInfo.name

        val cloudPlayer = PolocloudPlayer(player.name, player.uniqueId, serviceName)
        updatePolocloudPlayer(PlayerJoinEvent(cloudPlayer))
    }

    @EventHandler
    fun onDisconnect(event: PlayerDisconnectEvent) {
        val player = event.player
        val serverInfo = player.server?.info
        val serviceName = serverInfo?.name ?: "unknown"

        val cloudPlayer = PolocloudPlayer(player.name, player.uniqueId, serviceName)
        updatePolocloudPlayer(PlayerLeaveEvent(cloudPlayer))
    }


    @EventHandler
    fun kick(event: ServerKickEvent) {
        event.isCancelled = true
        event.cancelServer =  registeredFallbacks.filter { it.name != event.kickedFrom.name }.minByOrNull { it.players.size }
    }

    fun findFallback(): ServerInfo? {
        return registeredFallbacks.minByOrNull { it.players.size }
    }
}