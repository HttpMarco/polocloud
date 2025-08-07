package dev.httpmarco.polocloud.signs.abstraction.data.banner

import dev.httpmarco.polocloud.addons.api.location.Position
import dev.httpmarco.polocloud.signs.abstraction.layout.AnimationFrame
import dev.httpmarco.polocloud.signs.abstraction.data.BasedConnectorData
import java.util.concurrent.TimeUnit

class BannerData(group: String, position: Position, bannerLayout: BannerLayout) :
    BasedConnectorData<BannerData.BannerAnimationTick>(group,position, bannerLayout) {

    class Pattern(val color: BannerColor, val pattern: BannerPattern)

    class BannerAnimationTick(
        val baseColor: BannerColor,
        val patterns: List<Pattern>,
        period: Long,
        timeUnit: TimeUnit
    ) : AnimationFrame(period, timeUnit)

}