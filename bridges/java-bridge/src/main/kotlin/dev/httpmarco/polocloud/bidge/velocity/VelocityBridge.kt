package dev.httpmarco.polocloud.bidge.velocity

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer
import org.slf4j.Logger

@Plugin(id = "polocloud-bridge", name = "Polocloud bridge", version = "3.0.0.BETA", url = "https://github.com/HttpMarco/polocloud", description = "Polocloud-Bridge")
class VelocityBridge @Inject constructor(val server: ProxyServer, val logger: Logger) {

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {

    }
}