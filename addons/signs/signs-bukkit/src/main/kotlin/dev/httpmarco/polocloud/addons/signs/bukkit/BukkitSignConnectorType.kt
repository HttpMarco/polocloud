package dev.httpmarco.polocloud.addons.signs.bukkit

import dev.httpmarco.polocloud.signs.abstraction.types.signs.AbstractSignConnectorType
import dev.httpmarco.polocloud.addons.api.location.Position
import dev.httpmarco.polocloud.signs.abstraction.types.signs.SignFrame
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.block.sign.Side

class BukkitSignConnectorType(private val position: Position) : AbstractSignConnectorType<Material>() {

    private val sign: Sign? = Bukkit.getWorld(position.world)!!.getBlockAt(0, 0, 0).blockData as? Sign

    override fun display(frame: SignFrame) {
        for ((index, string) in frame.lines.withIndex()) {
            sign!!.getSide(Side.FRONT).setLine(index, string)
        }
    }

    override fun destroy() {
        // clear the sign lines
        this.display(SignFrame(listOf("", "", "", "")))
    }

    override fun isSupported(material: Material): Boolean {
        return material.data === org.bukkit.block.data.type.Sign::class.java
    }
}