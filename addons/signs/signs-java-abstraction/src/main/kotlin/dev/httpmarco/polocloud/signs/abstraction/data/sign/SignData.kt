package dev.httpmarco.polocloud.signs.abstraction.data.sign

import dev.httpmarco.polocloud.addons.api.location.Position
import dev.httpmarco.polocloud.signs.abstraction.layout.ConnectorLayout
import dev.httpmarco.polocloud.signs.abstraction.layout.AnimationFrame
import dev.httpmarco.polocloud.signs.abstraction.data.BasedConnectorData
import java.util.concurrent.TimeUnit

class SignData(group: String, position: Position, layout: SignLayout) :
    BasedConnectorData<SignData.SignAnimationTick>(group, position, layout) {

    class SignAnimationTick(val lines: Array<String>, period: Long = -1, timeUnit: TimeUnit = TimeUnit.SECONDS) :
        AnimationFrame(period, timeUnit)

}