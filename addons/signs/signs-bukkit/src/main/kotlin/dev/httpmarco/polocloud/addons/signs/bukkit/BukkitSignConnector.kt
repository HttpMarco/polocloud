package dev.httpmarco.polocloud.addons.signs.bukkit

import dev.httpmarco.polocloud.signs.abstraction.SignConnector
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignData
import dev.httpmarco.polocloud.v1.services.ServiceState
import org.bukkit.Bukkit
import org.bukkit.block.Sign
import org.bukkit.block.sign.Side

class BukkitSignConnector(data: SignData) : SignConnector(data) {

    private val sign: Sign?

    init {
        val position = data.position

        this.sign = Bukkit.getWorld(position.world)
            ?.getBlockAt(
                position.x.toInt(),
                position.y.toInt(),
                position.z.toInt()
            )?.state as Sign?

        display(data.layout.frames.get(ServiceState.ONLINE)!![0])
    }

    override fun display(frame: SignData.SignAnimationTick) {
        if (sign == null) {
            return
        }

        for ((i, line) in frame.lines.withIndex()) {
            sign.getSide(Side.FRONT).setLine(i, line)
        }
        sign.update(true, false)
    }
}