package dev.httpmarco.polocloud.bridges.waterdog

import dev.waterdog.waterdogpe.ProxyServer
import dev.waterdog.waterdogpe.plugin.Plugin

class WaterdogBridge : Plugin() {

    override fun onEnable() {
        for (info in ProxyServer.getInstance().servers) {
            ProxyServer.getInstance().removeServerInfo(info.serverName)
        }

        val bridgeInstance = WaterdogBridgeInstance()
        ProxyServer.getInstance().reconnectHandler = WaterdogReconnectHandler(bridgeInstance)
        ProxyServer.getInstance().joinHandler = bridgeInstance
    }
}