package dev.httpmarco.polocloud.addons.signs.bukkit

import dev.httpmarco.polocloud.signs.abstraction.SignConnector
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignData
import org.bukkit.Bukkit
import org.bukkit.block.Sign
import org.bukkit.block.sign.Side

class BukkitSignConnector(data: SignData) : SignConnector(data) {

    private val sign: Sign?

    init {
        val position = data.position

        val block = Bukkit.getWorld(position.world)!!.getBlockAt(
                position.x.toInt(),
                position.y.toInt(),
                position.z.toInt()
            )

        if(!block.chunk.isLoaded) {
            block.chunk.load()
        }

        this.sign = block.state as Sign?
        this.sign?.isWaxed = true
    }

    override fun display(frame: SignData.SignAnimationTick) {
        if (sign == null) {
            return
        }

        for (i in 0 until 4) {
            sign.getSide(Side.FRONT).setLine(i, replaceText(frame.lines.getOrNull(i) ?: " "))
        }

        Bukkit.getScheduler().callSyncMethod<Any>(BukkitBootstrap.plugin) {
            sign.update(true, false)
        }
    }
}