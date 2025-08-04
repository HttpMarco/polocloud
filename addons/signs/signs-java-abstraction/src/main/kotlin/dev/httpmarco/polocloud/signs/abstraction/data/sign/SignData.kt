package dev.httpmarco.polocloud.signs.abstraction.data.sign

import dev.httpmarco.polocloud.addons.api.location.Position
import dev.httpmarco.polocloud.signs.abstraction.data.AnimationFrame
import dev.httpmarco.polocloud.signs.abstraction.data.BasedConnectorData
import dev.httpmarco.polocloud.v1.services.ServiceState
import java.util.concurrent.TimeUnit

class SignData(position: Position, signFrames: Map<ServiceState, List<Pattern>>) : BasedConnectorData(position, signFrames) {

    class Pattern (val lines : Array<String>, period: Long, timeUnit: TimeUnit) : AnimationFrame(period, timeUnit)

}