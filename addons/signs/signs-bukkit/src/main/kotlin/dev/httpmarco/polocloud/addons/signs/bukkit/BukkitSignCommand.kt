package dev.httpmarco.polocloud.addons.signs.bukkit

import dev.httpmarco.polocloud.addons.api.location.Position
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BukkitSignCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender as Player

        if (args.size == 2) {
            if (args[0] == "add") {
                val group = args[1]
                val targetBlock = player.getTargetBlock(null, 7)

                if (targetBlock.isEmpty) {
                    player.sendMessage("§cYou must look at a block!")
                    return false
                }

                if (!BukkitConnectors.isSupported(targetBlock.type)) {
                    player.sendMessage("§cThis block type is not supported!")
                    return false
                }


                BukkitConnectors.attachConnector(
                    group,
                    Position(
                        player.world.name,
                        targetBlock.x.toDouble(),
                        targetBlock.y.toDouble(),
                        targetBlock.z.toDouble()
                    ), targetBlock.type
                )

                player.sendMessage("§aConnector added at ${targetBlock.x}, ${targetBlock.y}, ${targetBlock.z} in world ${player.world.name}")
            }
        }

        return false;
    }
}