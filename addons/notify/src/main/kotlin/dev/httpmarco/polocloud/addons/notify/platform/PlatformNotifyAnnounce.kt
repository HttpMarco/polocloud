package dev.httpmarco.polocloud.addons.notify.platform

import dev.httpmarco.polocloud.addons.api.replacePlaceHolders
import dev.httpmarco.polocloud.addons.notify.NotifyAddon
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.events.definitions.service.ServiceChangeStateEvent
import dev.httpmarco.polocloud.shared.service.Service
import dev.httpmarco.polocloud.v1.services.ServiceState
import java.io.File

class PlatformNotifyAnnounce(miniMessages: Boolean = false, val alert : (Pair<String, String>) -> (Any)) {

    private var notifyAddon: NotifyAddon = NotifyAddon(File("plugins/polocloud"), miniMessages)

    init {
        Polocloud.instance().eventProvider().subscribe(ServiceChangeStateEvent::class.java) { event ->
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
        val message = replacePlaceHolders(config.messages(messageKey), service)

        alert(Pair(permission, config.prefix() + message))
    }
}