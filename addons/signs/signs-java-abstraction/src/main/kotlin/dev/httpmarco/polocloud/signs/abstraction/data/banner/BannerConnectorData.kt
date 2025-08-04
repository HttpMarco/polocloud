package dev.httpmarco.polocloud.signs.abstraction.data.banner

import dev.httpmarco.polocloud.addons.api.location.Position
import dev.httpmarco.polocloud.signs.abstraction.data.AnimationFrame
import dev.httpmarco.polocloud.signs.abstraction.data.BasedConnectorData
import dev.httpmarco.polocloud.v1.services.ServiceState
import java.util.concurrent.TimeUnit

class BannerConnectorData(position: Position, patternFrame: Map<ServiceState, List<PatternLayout>>) : BasedConnectorData(position, patternFrame) {

    class Pattern (val color: BannerColor, val pattern: BannerPattern)

    class PatternLayout(val baseColor : BannerColor, val patterns: List<Pattern>, period: Long, timeUnit: TimeUnit) : AnimationFrame(period, timeUnit)

}