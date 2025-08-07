package dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ProxyServer
import dev.httpmarco.polocloud.addons.proxy.CloudSubCommand
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import net.kyori.adventure.text.minimessage.MiniMessage

class PlayersSubCommand(
    private val proxyAddon: ProxyAddon,
    private val proxyServer: ProxyServer
) : CloudSubCommand {

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
                config.prefix() + "<gray>No players are currently online."
            ))
            return
        }

        source.sendMessage(miniMessage.deserialize(
            config.prefix() + "<gradient:#00fdee:#118bd1><bold>Online Players (${onlinePlayers.size})</bold></gradient>"
        ))

        for (player in onlinePlayers) {
            val currentServer = player.currentServer.orElse(null)
            val serverName = currentServer?.serverInfo?.name ?: "Unknown"
            
            source.sendMessage(miniMessage.deserialize(
                config.prefix() + "<aqua>${player.username}</aqua> <gray>→</gray> <green>$serverName</green>"
            ))
        }

        val serverCount = onlinePlayers.map { it.currentServer.orElse(null)?.serverInfo?.name }.distinct().size
        source.sendMessage(miniMessage.deserialize(
            config.prefix() + "<gray>Distributed across $serverCount server(s)</gray>"
        ))
    }
} 