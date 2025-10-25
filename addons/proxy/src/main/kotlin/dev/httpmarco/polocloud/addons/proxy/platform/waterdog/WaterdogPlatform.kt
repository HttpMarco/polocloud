package dev.httpmarco.polocloud.addons.proxy.platform.waterdog

import dev.waterdog.waterdogpe.ProxyServer
import dev.waterdog.waterdogpe.event.defaults.ProxyPingEvent
import dev.waterdog.waterdogpe.plugin.Plugin
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.platform.waterdog.events.PlayerLoginEvent
import dev.httpmarco.polocloud.addons.proxy.platform.waterdog.events.WaterdogMotdUpdater
import java.io.File

private lateinit var proxyAddon: ProxyAddon

class WaterdogPlatform: Plugin() {

    override fun onEnable() {
        proxyAddon = ProxyAddon(File("plugins/polocloud"), false)
        val config = proxyAddon.config

        ProxyServer.getInstance().pluginManager
        proxy.commandMap.registerCommand(
            WaterdogCloudCommand(
                proxyAddon
            )
        )

        val loginEvent = PlayerLoginEvent(this, config)
        val motdUpdater = WaterdogMotdUpdater(this, config)

        ProxyServer.getInstance().eventManager.subscribe(ProxyPingEvent::class.java, motdUpdater::onProxyPing)
        ProxyServer.getInstance().eventManager.subscribe(dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent::class.java, loginEvent::onPlayerLogin)
    }

    fun proxyAddon(): ProxyAddon {
        return proxyAddon
    }
}