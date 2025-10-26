package dev.httpmarco.polocloud.addons.signs.nukkit

import cn.nukkit.Server
import cn.nukkit.blockentity.BlockEntitySign
import cn.nukkit.math.Vector3
import dev.httpmarco.polocloud.signs.abstraction.SignConnector
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignData

class NukkitSignConnector(data: SignData) : SignConnector(data) {

    private val sign: BlockEntitySign?

    init {
        val position = data.position

        val block = Server.getInstance().getLevelByName(position.world)!!.getBlockEntity(
            Vector3(
                position.x.toDouble(),
                position.y.toDouble(),
                position.z.toDouble()
            )
        )

        if(!block.chunk.isLoaded) {
            block.chunk.load()
        }

        this.sign = block as BlockEntitySign?
    }

    override fun display(frame: SignData.SignAnimationTick) {
        if (sign == null) {
            return
        }

        val lines = Array(4) { i ->
            replaceText(frame.lines.getOrNull(i) ?: " ").replace("\\u00a7", "§")
        }

        sign.setText(*lines)
    }
}