package dev.httpmarco.polocloud.addons.notify.platform.bungeecord

import dev.httpmarco.polocloud.addons.notify.NotifyAddon
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.events.definitions.service.ServiceChangeStateEvent
import dev.httpmarco.polocloud.shared.service.Service
import dev.httpmarco.polocloud.v1.services.ServiceState
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Plugin
import java.io.File

private lateinit var notifyAddon: NotifyAddon

class BungeecordPlatform: Plugin() {

    override fun onEnable() {
        notifyAddon = NotifyAddon(File("plugins/polocloud"), false)

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
        var message = config.messages(messageKey)
        message = message
            .replace("%service%", service.groupName + "-" + service.id)
            .replace("%group%", service.groupName)
            .replace("%type%", service.type.toString())
            .replace("%id%", service.id.toString())

        ProxyServer.getInstance().players.forEach { player ->
            if (player.hasPermission(permission)) {
                player.sendMessage(TextComponent(config.prefix() + message))
            }
        }
    }
}