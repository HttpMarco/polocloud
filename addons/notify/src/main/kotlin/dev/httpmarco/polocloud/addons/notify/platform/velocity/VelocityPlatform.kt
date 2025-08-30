package dev.httpmarco.polocloud.addons.notify.platform.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.proxy.ProxyServer
import dev.httpmarco.polocloud.addons.api.MiniMessageFormatter
import dev.httpmarco.polocloud.addons.notify.NotifyAddon
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.events.definitions.service.ServiceChangeStateEvent
import dev.httpmarco.polocloud.shared.service.Service
import dev.httpmarco.polocloud.v1.services.ServiceState
import net.kyori.adventure.text.minimessage.MiniMessage
import org.slf4j.Logger
import java.io.File

class VelocityPlatform @Inject constructor(
    private val server: ProxyServer,
    private val logger: Logger
) {

    private lateinit var notifyAddon: NotifyAddon
    private val miniMessage = MiniMessage.miniMessage()

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        this.notifyAddon = NotifyAddon(File("plugins/polocloud"), true)

        Polocloud.instance().eventProvider().subscribe(ServiceChangeStateEvent::class.java) {event ->
            val service = event.service

            if (service.state == ServiceState.STARTING) {
                this.announceNotification("service_starting", service, "polocloud.addons.notify.receive.starting")
            }

            if (service.state == ServiceState.ONLINE) {
                this.announceNotification("service_online", service, "polocloud.addons.notify.receive.online")
            }

            if (service.state == ServiceState.STOPPED) {
                this.announceNotification("service_shutdown", service, "polocloud.addons.notify.receive.shutdown")
            }
        }
    }

    private fun announceNotification(messageKey: String, service: Service, permission: String) {
        val config = notifyAddon.config
        val message = config.messages(messageKey)
        val formattedMessage = MiniMessageFormatter.format(message)
            .replace("%service%", service.groupName + "-" + service.id)
            .replace("%group%", service.groupName)
            .replace("%type%", service.type.toString())
            .replace("%id%", service.id.toString())

        server.allPlayers.forEach { player ->
            if (player.hasPermission(permission)) {
                player.sendMessage(miniMessage.deserialize(config.prefix() + formattedMessage))
            }
        }
    }
}