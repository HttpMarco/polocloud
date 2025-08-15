package dev.httpmarco.polocloud.addons.proxy.platform.bungeecord

import net.md_5.bungee.api.CommandSender

interface BungeecordCloudSubCommand {
    fun execute(source: CommandSender, args: List<String?>)
}