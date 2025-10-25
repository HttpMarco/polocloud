package dev.httpmarco.polocloud.addons.proxy.platform.waterdog.commands

import dev.waterdog.waterdogpe.ProxyServer
import dev.waterdog.waterdogpe.command.CommandSender
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.platform.waterdog.WaterdogCloudSubCommand

class PlayersSubCommand(
    val proxyAddon: ProxyAddon
) : WaterdogCloudSubCommand {

    override fun execute(sender: CommandSender, args: List<String?>) {
        val config = this.proxyAddon.config

        if (!sender.hasPermission("polocloud.addons.proxy.command.cloud.players")) {
            sender.sendMessage(config.prefix() + config.messages("no_permission"))
            return
        }

        val onlinePlayers = ProxyServer.getInstance().players.toList()

        if (onlinePlayers.isEmpty()) {
            sender.sendMessage(
                config.prefix() + config.messages("no_players_online_players")
            )
            return
        }

        sender.sendMessage(
            config.prefix() + config.messages("players_header")
                .replace("%count%", onlinePlayers.size.toString())
        )

        for (player in onlinePlayers) {
            val currentServer = player.component2().serverInfo
            val serverName = currentServer?.serverName ?: "Unknown"

            sender.sendMessage(
                config.prefix() + config.messages("player_server_info")
                    .replace("%player%", player.component2().name)
                    .replace("%server%", serverName)
            )
        }

        val serverCount = onlinePlayers.map { it.second?.serverInfo?.serverName }.distinct().size
        sender.sendMessage(
            config.prefix() + config.messages("players_footer")
                .replace("%serverCount%", serverCount.toString())
        )
    }
}