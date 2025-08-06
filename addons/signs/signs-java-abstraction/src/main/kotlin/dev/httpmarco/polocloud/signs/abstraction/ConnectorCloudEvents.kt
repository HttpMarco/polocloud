package dev.httpmarco.polocloud.signs.abstraction

import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.events.definitions.ServiceChangePlayerCountEvent
import dev.httpmarco.polocloud.shared.events.definitions.ServiceOnlineEvent
import dev.httpmarco.polocloud.shared.events.definitions.ServiceShutdownEvent

class ConnectorCloudEvents(connectors: Connectors<*>) {

    init {
        Polocloud.instance().eventProvider().subscribe(ServiceOnlineEvent::class.java) {
            if (it.service.name() != Polocloud.instance().selfServiceName()) {
                val originalConnector = connectors.findAttachConnector(it.service)
                if (originalConnector == null) {
                    var connector = connectors.findEmptyConnector(it.service.groupName)

                    connector?.bindWith(it.service)
                }
            }
        }

        Polocloud.instance().eventProvider().subscribe(ServiceShutdownEvent::class.java) {
            val connector = connectors.findAttachConnector(it.service)

            if (connector != null) {
                connector.unbind()
                // todo filter a service from the list of connectors
            }
        }

        Polocloud.instance().eventProvider().subscribe(ServiceChangePlayerCountEvent::class.java) {
            val connector = connectors.findAttachConnector(it.service)

            if (connector != null) {
                // update the displayed service again to refresh the visual output
                connector.bindWith(it.service)
            }
        }
    }
}