package dev.httpmarco.polocloud.addons.signs.bukkit

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BukkitSignCommand : Command("signs") {

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        val player = sender as Player

        if (args.size == 2) {
            if (args[0] == "add") {
                val targetBlock = player.getTargetBlock(null, 7)

                if (targetBlock.isEmpty) {
                    player.sendMessage("§cYou must look at a block!")
                    return false
                }
            }
        }

        return false;
    }
}