package dev.httpmarco.polocloud.addons.signs.bukkit

import dev.httpmarco.polocloud.signs.abstraction.Connector
import dev.httpmarco.polocloud.signs.abstraction.Connectors
import dev.httpmarco.polocloud.signs.abstraction.data.BasedConnectorData
import dev.httpmarco.polocloud.signs.abstraction.data.banner.BannerData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignData

class BukkitConnectors : Connectors() {

    override fun mapData(data: BasedConnectorData): Connector {
        when(data) {
            is SignData -> BukkitSignConnector()
            is BannerData -> BukkitBannerConnector()
        }
    }
}