package dev.httpmarco.polocloud.signs.abstraction.data

import dev.httpmarco.polocloud.addons.api.location.Position
import dev.httpmarco.polocloud.v1.services.ServiceState

open class BasedConnectorData(val position: Position, val frames: Map<ServiceState, List<AnimationFrame>>) {

}