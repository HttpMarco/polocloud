package dev.httpmarco.polocloud.addons.signs.bukkit

import dev.httpmarco.polocloud.addons.api.location.Position
import dev.httpmarco.polocloud.signs.abstraction.Connector
import dev.httpmarco.polocloud.signs.abstraction.ConnectorSupport
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignLayout
import org.bukkit.Material
import org.bukkit.block.data.type.WallSign

class BukkitSignSupport : ConnectorSupport<Material, SignLayout> {

    override fun isSupported(material: Material): Boolean {
        return material.data == WallSign::class.java
    }

    override fun handledConnector(group: String, position: Position): Connector<*> {
        return BukkitSignConnector(SignData(group, position, BukkitConnectors.generateSignLayout()))
    }
}