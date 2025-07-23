package dev.httpmarco.polocloud.bridges.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.KickedFromServerEvent
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.server.RegisteredServer
import com.velocitypowered.api.proxy.server.ServerInfo
import dev.httpmarco.polocloud.bridge.api.BridgeInstance
import org.slf4j.Logger
import java.net.InetSocketAddress
import kotlin.jvm.optionals.getOrNull

@Plugin(
    id = "polocloud-bridge",
    name = "Polocloud-Bridge",
    version = "3.0.0-SNAPSHOT",
    authors = ["Polocloud"],
    url = "https://github.com/HttpMarco/polocloud",
    description = "Polocloud-Bridge"
)
class VelocityBridge @Inject constructor(val proxyServer: ProxyServer, logger: Logger) : BridgeInstance<ServerInfo>() {

    private val registeredFallbacks = ArrayList<RegisteredServer>()

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
    }

    @Subscribe
    fun onConnect(event: PlayerChooseInitialServerEvent) {
        event.setInitialServer(registeredFallbacks.minByOrNull { it.playersConnected.size })
    }

    @Subscribe
    fun onKick(event: KickedFromServerEvent) {
        TODO()
    }

    override fun generateInfo(name: String, hostname: String, port: Int): ServerInfo {
        return ServerInfo(name, InetSocketAddress(hostname, port))
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