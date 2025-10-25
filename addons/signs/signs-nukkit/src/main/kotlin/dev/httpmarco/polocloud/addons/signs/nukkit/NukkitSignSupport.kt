package dev.httpmarco.polocloud.addons.signs.nukkit

import cn.nukkit.block.Block
import cn.nukkit.block.BlockID
import dev.httpmarco.polocloud.addons.api.location.Position
import dev.httpmarco.polocloud.signs.abstraction.Connector
import dev.httpmarco.polocloud.signs.abstraction.ConnectorSupport
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignLayout

class NukkitSignSupport: ConnectorSupport<Block, SignLayout> {

    override fun isSupported(material: Block): Boolean {
        return material.id == BlockID.SIGN_POST
    }

    override fun handledConnector(group: String, position: Position): Connector<*> {
        return NukkitSignConnector(
            SignData(
                group,
                position,
                NukkitConnectors.generateSignLayout()
            )
        )
    }
}