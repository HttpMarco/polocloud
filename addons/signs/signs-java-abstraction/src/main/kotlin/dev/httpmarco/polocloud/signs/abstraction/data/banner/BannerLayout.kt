package dev.httpmarco.polocloud.signs.abstraction.data.banner

import dev.httpmarco.polocloud.signs.abstraction.layout.ConnectorLayout
import dev.httpmarco.polocloud.v1.services.ServiceState

class BannerLayout(id: String, frames: Map<ServiceState, List<BannerData.BannerAnimationTick>>) :
    ConnectorLayout<BannerData.BannerAnimationTick>(id, frames) {
}