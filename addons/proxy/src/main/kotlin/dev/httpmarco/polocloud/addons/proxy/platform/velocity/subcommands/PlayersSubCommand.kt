package dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ProxyServer
import dev.httpmarco.polocloud.addons.proxy.platform.velocity.VelocityCloudSubCommand
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import net.kyori.adventure.text.minimessage.MiniMessage

class PlayersSubCommand(
    private val proxyAddon: ProxyAddon,
    private val proxyServer: ProxyServer
) : VelocityCloudSubCommand {

    private val miniMessage = MiniMessage.miniMessage()

    override fun execute(source: CommandSource, arguments: List<String>) {
        val config = this.proxyAddon.config

        if (!source.hasPermission("polocloud.addons.proxy.command.cloud.players")) {
            source.sendMessage(miniMessage.deserialize(config.prefix() + config.messages("no_permission")))
             return
         }

        val onlinePlayers = proxyServer.allPlayers.toList()

        if (onlinePlayers.isEmpty()) {
            source.sendMessage(miniMessage.deserialize(
                config.prefix() + config.messages("no_players_online_players")
            ))
            return
        }

        source.sendMessage(miniMessage.deserialize(
            config.prefix() + config.messages("players_header")
                .replace("%count%", onlinePlayers.size.toString())
        ))

        for (player in onlinePlayers) {
            val currentServer = player.currentServer.orElse(null)
            val serverName = currentServer?.serverInfo?.name ?: "Unknown"
            
            source.sendMessage(miniMessage.deserialize(
                config.prefix() + config.messages("player_server_info")
                    .replace("%player%", player.username)
                    .replace("%server%", serverName)
            ))
        }

        val serverCount = onlinePlayers.map { it.currentServer.orElse(null)?.serverInfo?.name }.distinct().size
        source.sendMessage(miniMessage.deserialize(
            config.prefix() + config.messages("players_footer")
                .replace("%serverCount%", serverCount.toString())
        ))
    }
} 