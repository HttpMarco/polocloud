package dev.httpmarco.polocloud.addons.signs.bukkit

import dev.httpmarco.polocloud.signs.abstraction.Connectors
import dev.httpmarco.polocloud.signs.abstraction.data.banner.BannerData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignData
import org.bukkit.Material

object BukkitConnectors : Connectors<Material>() {

    init {
        bindSupport(BukkitSignSupport())
    }

    override fun generateSignConnector(data: SignData) = BukkitSignConnector(data)

    override fun generateBannerConnector(data: BannerData) = BukkitBannerConnector(data)

}