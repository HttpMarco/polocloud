package dev.httpmarco.polocloud.addons.signs.bukkit

import dev.httpmarco.polocloud.signs.abstraction.ConnectorSupport
import org.bukkit.Material
import org.bukkit.block.data.type.Sign

class BukkitSignSupport : ConnectorSupport<Material> {

    override fun isSupported(material: Material): Boolean {
        return material.data::class === Sign::class
    }
}