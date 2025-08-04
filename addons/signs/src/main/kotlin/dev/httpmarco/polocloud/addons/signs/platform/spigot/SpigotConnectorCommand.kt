package dev.httpmarco.polocloud.addons.signs.platform.spigot

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class SpigotConnectorCommand : Command("signs") {

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {

        if(args.size == 2) {
            if(args[0] == "add") {

            }
        }

        if(args.size == 1) {
            if(args[0] == "remove") {

            }
        }

        return false
    }
}