package dev.httpmarco.polocloud.signs.abstraction.data

import dev.httpmarco.polocloud.addons.api.location.Position
import dev.httpmarco.polocloud.signs.abstraction.layout.AnimationFrame
import dev.httpmarco.polocloud.signs.abstraction.layout.ConnectorLayout
import dev.httpmarco.polocloud.v1.services.ServiceState

open class BasedConnectorData<A  : AnimationFrame>(val position: Position, val layout: ConnectorLayout<A>) {

}