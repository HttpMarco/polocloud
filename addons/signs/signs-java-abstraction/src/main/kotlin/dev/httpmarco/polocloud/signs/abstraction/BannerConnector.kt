package dev.httpmarco.polocloud.signs.abstraction

import dev.httpmarco.polocloud.signs.abstraction.data.banner.BannerData

abstract class BannerConnector(data: BannerData) : Connector<BannerData.BannerAnimationTick>(data) {
}