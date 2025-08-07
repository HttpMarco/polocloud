package dev.httpmarco.polocloud.addons.proxy.platform.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.proxy.ProxyServer
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
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

        VelocityTablistUpdater( this, server, config)
    }
}