package dev.httpmarco.polocloud.signs.abstraction

import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.events.definitions.service.ServiceChangePlayerCountEvent
import dev.httpmarco.polocloud.shared.events.definitions.service.ServiceChangeStateEvent
import dev.httpmarco.polocloud.v1.services.ServiceState

class ConnectorCloudEvents(connectors: Connectors<*>) {

    init {
        Polocloud.instance().eventProvider().subscribe(ServiceChangeStateEvent::class.java) { event ->
            val service = event.service

            if (service.state == ServiceState.ONLINE) {
                if (service.name() != Polocloud.instance().selfServiceName()) {
                    val originalConnector = connectors.findAttachConnector(service)
                    if (originalConnector == null) {
                        var connector = connectors.findEmptyConnector(service.groupName)

                        connector?.bindWith(service)
                    }
                }
            }

            if (service.state == ServiceState.STOPPING) {
                val connector = connectors.findAttachConnector(service)

                if (connector != null) {
                    connector.unbind()
                    // todo filter a service from the list of connectors
                }
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