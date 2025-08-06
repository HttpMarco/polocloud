package dev.httpmarco.polocloud.addons.notify.platform.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.proxy.ProxyServer
import dev.httpmarco.polocloud.addons.api.MiniMessageFormatter
import dev.httpmarco.polocloud.addons.notify.NotifyAddon
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.events.definitions.ServiceOnlineEvent
import dev.httpmarco.polocloud.shared.events.definitions.ServiceShutdownEvent
import dev.httpmarco.polocloud.shared.events.definitions.ServiceStartingEvent
import dev.httpmarco.polocloud.shared.service.Service
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
        val config = notifyAddon.config

        Polocloud.instance().eventProvider().subscribe(ServiceStartingEvent::class.java) {
            this.announceNotification("service_starting", it.service, "polocloud.addons.notify.receive.starting")
        }

        Polocloud.instance().eventProvider().subscribe(ServiceOnlineEvent::class.java) {
            this.announceNotification("service_online", it.service, "polocloud.addons.notify.receive.online")
        }

        Polocloud.instance().eventProvider().subscribe(ServiceShutdownEvent::class.java) {
            this.announceNotification("service_shutdown", it.service, "polocloud.addons.notify.receive.shutdown")
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