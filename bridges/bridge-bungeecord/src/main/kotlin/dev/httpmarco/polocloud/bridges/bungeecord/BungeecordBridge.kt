package dev.httpmarco.polocloud.bridges.bungeecord

import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.plugin.Plugin
import org.bstats.bungeecord.Metrics

class BungeecordBridge : Plugin() {

    override fun onEnable() {
        ProxyServer.getInstance().servers.clear()

        val bridgeInstance = BungeecordBridgeInstance()
        ProxyServer.getInstance().reconnectHandler = BungeecordReconnectHandler(bridgeInstance)
        ProxyServer.getInstance().pluginManager.registerListener(this, bridgeInstance)

        val pluginId = 26764
        Metrics(this, pluginId)
        logger.info("bStats Metrics successfully initialized for ${this.javaClass.simpleName} (pluginId=$pluginId)")
    }
}
