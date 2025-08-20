package dev.httpmarco.polocloud.bridges.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.player.KickedFromServerEvent
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent
import com.velocitypowered.api.event.player.ServerConnectedEvent
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.server.RegisteredServer
import com.velocitypowered.api.proxy.server.ServerInfo
import dev.httpmarco.polocloud.bridge.api.BridgeInstance
import dev.httpmarco.polocloud.shared.events.definitions.PlayerJoinEvent
import dev.httpmarco.polocloud.shared.events.definitions.PlayerLeaveEvent
import dev.httpmarco.polocloud.shared.player.PolocloudPlayer
import dev.httpmarco.polocloud.shared.service.Service
import org.bstats.velocity.Metrics
import org.slf4j.Logger
import java.net.InetSocketAddress
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

class VelocityBridge @Inject constructor(val proxyServer: ProxyServer, private val logger: Logger, val metricsFactory: Metrics.Factory) : BridgeInstance<ServerInfo>() {


    private val registeredFallbacks = ArrayList<RegisteredServer>()

    private lateinit var metrics: Metrics

    init {
        // remove all registered servers on startup
        proxyServer.allServers.forEach {
            proxyServer.unregisterServer(it.serverInfo)
        }
        logger.debug("Unregistered all servers on startup by Polocloud-Bridge")
    }

    @Subscribe
    fun onInitialize(event: ProxyInitializeEvent) {
        super.initialize()
        val pluginId = 26763 
        metrics = metricsFactory.make(this, pluginId)
    }

    @Subscribe
    fun onConnect(event: PlayerChooseInitialServerEvent) {
        event.setInitialServer(registeredFallbacks.minByOrNull { it.playersConnected.size })
    }

    @Subscribe
    fun onPlayerJoin(event: ServerConnectedEvent) {
        val player = event.player
        val serviceName = event.server.serverInfo.name

        updatePolocloudPlayer(PlayerJoinEvent(PolocloudPlayer(player.username, player.uniqueId, serviceName)))
    }

    @Subscribe
    fun onDisconnect(event: DisconnectEvent) {
        val player = event.player

        val serviceName = player.currentServer
            .flatMap { Optional.ofNullable(it.serverInfo.name) }
            .orElse(null)
        if(serviceName == null) {
            // Player was not connected to any service
            return
        }

        updatePolocloudPlayer(PlayerLeaveEvent(PolocloudPlayer(player.username, player.uniqueId, serviceName)))
    }

    @Subscribe
    fun onKick(event: KickedFromServerEvent) {
        if (event.player.isActive) {
            if(event.server.serverInfo == null) {
                // Player was not connected to any service
                return
            }
            event.result =
                KickedFromServerEvent.RedirectPlayer.create(registeredFallbacks.filter { it.serverInfo.name != event.server.serverInfo.name }
                    .minByOrNull { it.playersConnected.size })
        }
    }

    override fun generateInfo(service: Service): ServerInfo {
        return ServerInfo(service.name(), InetSocketAddress(service.hostname, service.port))
    }

    override fun registerService(identifier: ServerInfo, fallback: Boolean) {
        val server = proxyServer.registerServer(identifier)

        if (fallback) {
            registeredFallbacks.add(server)
        }
    }

    override fun unregisterService(identifier: ServerInfo) {
        proxyServer.unregisterServer(identifier)
    }

    override fun findInfo(name: String): ServerInfo? {
        return proxyServer.getServer(name).map { it -> it.serverInfo }.getOrNull()
    }
}
