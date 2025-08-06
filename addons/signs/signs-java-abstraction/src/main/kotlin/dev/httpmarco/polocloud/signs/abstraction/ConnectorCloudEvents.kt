package dev.httpmarco.polocloud.signs.abstraction

import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.events.definitions.ServiceOnlineEvent
import dev.httpmarco.polocloud.shared.events.definitions.ServiceShutdownEvent

class ConnectorCloudEvents {

    init {
        Polocloud.instance().eventProvider().subscribe(ServiceOnlineEvent::class.java) {
            var connector = Connectors.context.findEmptyConnector(it.service.groupName)

            if (connector !== null) {
                connector.bindWith(it.service)
            }
        }

        Polocloud.instance().eventProvider().subscribe(ServiceShutdownEvent::class.java) {
            val connector = Connectors.context.findAttachConnector(it.service)

            if (connector != null) {
                connector.unbind()
            }
        }
    }
}