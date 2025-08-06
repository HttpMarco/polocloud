package dev.httpmarco.polocloud.signs.abstraction

import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.events.definitions.ServiceOnlineEvent
import dev.httpmarco.polocloud.shared.events.definitions.ServiceShutdownEvent

class ConnectorCloudEvents(connectors: Connectors<*>) {

    init {
        Polocloud.instance().eventProvider().subscribe(ServiceOnlineEvent::class.java) {
            if (it.service.name() != Polocloud.instance().selfServiceName()) {
                var connector = connectors.findEmptyConnector(it.service.groupName)

                if (connector !== null) {
                    connector.bindWith(it.service)
                }
            }
        }

        Polocloud.instance().eventProvider().subscribe(ServiceShutdownEvent::class.java) {
            val connector = connectors.findAttachConnector(it.service)

            if (connector !== null) {
                connector.unbind()
            }
        }
    }
}