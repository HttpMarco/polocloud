package dev.httpmarco.polocloud.addons.notify.platform.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.proxy.ProxyServer
import dev.httpmarco.polocloud.addons.api.MiniMessageFormatter
import dev.httpmarco.polocloud.addons.notify.NotifyAddon
import dev.httpmarco.polocloud.addons.notify.platform.PlatformNotifyAnnounce
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.events.definitions.service.ServiceChangeStateEvent
import dev.httpmarco.polocloud.shared.service.Service
import dev.httpmarco.polocloud.v1.services.ServiceState
import net.kyori.adventure.text.minimessage.MiniMessage
import org.slf4j.Logger
import java.io.File

class VelocityPlatform @Inject constructor(
    private val server: ProxyServer,
) {

    private val miniMessage = MiniMessage.miniMessage()

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        PlatformNotifyAnnounce(true, alert = {
            server.allPlayers.forEach { player ->
                if (player.hasPermission(it.first)) {
                    player.sendMessage(miniMessage.deserialize(it.second))
                }
            }
        })
    }
}