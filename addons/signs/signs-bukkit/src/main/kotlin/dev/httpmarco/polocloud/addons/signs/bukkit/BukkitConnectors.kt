package dev.httpmarco.polocloud.addons.signs.bukkit

import dev.httpmarco.polocloud.signs.abstraction.Connectors
import dev.httpmarco.polocloud.signs.abstraction.data.banner.BannerData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignData
import org.bukkit.Material

class BukkitConnectors : Connectors<Material>() {

    override fun generateSignConnector(data: SignData) = BukkitSignConnector(data)

    override fun generateBannerConnector(data: BannerData) = BukkitBannerConnector(data)

}