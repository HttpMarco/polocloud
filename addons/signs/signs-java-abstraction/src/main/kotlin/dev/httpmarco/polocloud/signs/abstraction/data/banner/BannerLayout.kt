package dev.httpmarco.polocloud.signs.abstraction.data.banner

import dev.httpmarco.polocloud.signs.abstraction.ConnectorState
import dev.httpmarco.polocloud.signs.abstraction.layout.ConnectorLayout

class BannerLayout(id: String, frames: Map<ConnectorState, List<BannerData.BannerAnimationTick>>) :
    ConnectorLayout<BannerData.BannerAnimationTick>(id, frames) {
}