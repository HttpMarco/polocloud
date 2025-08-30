package dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.commands

import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.BungeecordCloudSubCommand
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent

class PlayersSubCommand(
    val proxyAddon: ProxyAddon
) : BungeecordCloudSubCommand {

    override fun execute(sender: CommandSender, args: List<String?>) {
        val config = this.proxyAddon.config

        if (!sender.hasPermission("polocloud.addons.proxy.command.cloud.players")) {
            sender.sendMessage(TextComponent(config.prefix() + config.messages("no_permission")))
            return
        }

        val onlinePlayers = ProxyServer.getInstance().players.toList()

        if (onlinePlayers.isEmpty()) {
            sender.sendMessage(TextComponent(
                config.prefix() + config.messages("no_players_online_players")
            ))
            return
        }

        sender.sendMessage(TextComponent(
            config.prefix() + config.messages("players_header")
                .replace("%count%", onlinePlayers.size.toString())
        ))

        for (player in onlinePlayers) {
            val currentServer = player.server
            val serverName = currentServer?.info?.name ?: "Unknown"

            sender.sendMessage(TextComponent(
                config.prefix() + config.messages("player_server_info")
                    .replace("%player%", player.name)
                    .replace("%server%", serverName)
            ))
        }

        val serverCount = onlinePlayers.map { it.server?.info?.name }.distinct().size
        sender.sendMessage(TextComponent(
            config.prefix() + config.messages("players_footer")
                .replace("%serverCount%", serverCount.toString())
        ))

    }

}