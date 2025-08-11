package dev.httpmarco.polocloud.addons.proxy.platform.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.proxy.ProxyServer
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.platform.velocity.events.PlayerLoginEvent
import dev.httpmarco.polocloud.addons.proxy.platform.velocity.events.VelocityMotdUpdater
import org.slf4j.Logger
import java.io.File

class VelocityPlatform @Inject constructor(
    private val server: ProxyServer,
    private val logger: Logger
) {

    private lateinit var proxyAddon: ProxyAddon

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        this.proxyAddon = ProxyAddon(File("plugins/polocloud"), true)
        val config = proxyAddon.config

        val commandManager = this.server.commandManager
        commandManager.register(
            commandManager.metaBuilder("polocloud").aliases(*this.proxyAddon.config.aliases().toTypedArray()).plugin(this).build(),
            VelocityCloudCommand(this.proxyAddon, server),
        )

        val eventManager = this.server.eventManager
        eventManager.register(this, VelocityMotdUpdater(this, server, config))
        eventManager.register(this, PlayerLoginEvent(this, server, config))

        VelocityTablistUpdater( this, server, config)
    }

    fun proxyAddon(): ProxyAddon {
        return this.proxyAddon
    }
}