package dev.httpmarco.polocloud.addons.signs.nukkit

import cn.nukkit.Player
import cn.nukkit.block.Block
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import dev.httpmarco.polocloud.addons.api.location.Position

class NukkitSignCommand: Command(
    "signs",
    "Create / Remove Cloud Signs",
) {

    init {
        this.permission = "polocloud.command.sign"
    }

    override fun execute(p0: CommandSender?, p1: String?, p2: Array<out String?>?): Boolean {
        if (!testPermission(p0)) {
            return false
        }

        val player = p0 as Player

        if (p2?.size == 2) {
            if (p2[0] == "add") {
                val group = p2[1]
                val targetBlock = player.getTargetBlock(7)

                if (targetBlock == null) {
                    player.sendMessage("§cYou must look at a block!")
                    return false
                }

                if (!NukkitConnectors.isSupported(targetBlock)) {
                    player.sendMessage("§cThis block type is not supported!")
                    return false
                }

                NukkitConnectors.attachConnector(
                    group.toString(),
                    Position(
                        player.level.folderName,
                        targetBlock.x.toDouble(),
                        targetBlock.y.toDouble(),
                        targetBlock.z.toDouble()
                    ), targetBlock
                )

                player.sendMessage("§aConnector added at ${targetBlock.x}, ${targetBlock.y}, ${targetBlock.z} in world ${player.level.folderName}")
            }
        }

        return false;
    }
}