package dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ProxyServer
import dev.httpmarco.polocloud.addons.proxy.CloudSubCommand
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import net.kyori.adventure.text.minimessage.MiniMessage

class KickAllSubCommand(
    private val proxyAddon: ProxyAddon,
    private val proxyServer: ProxyServer
) : CloudSubCommand {

    private val miniMessage = MiniMessage.miniMessage()

    override fun execute(source: CommandSource, arguments: List<String>) {
        val config = this.proxyAddon.config

        // if (!source.hasPermission("polocloud.addons.proxy.command.cloud.kickall")) {
        //     source.sendMessage(miniMessage.deserialize(config.prefix() + config.messages("no_permission")))
        //     return
        // }

        val allPlayers = proxyServer.allPlayers.toList()
        
        if (allPlayers.isEmpty()) {
            source.sendMessage(miniMessage.deserialize(
                config.prefix() + "<gray>No players are currently online."
            ))
            return
        }

        // Check if a specific server was specified
        val targetServer = if (arguments.isNotEmpty()) arguments[0] else null
        
        val playersToKick = if (targetServer != null) {
            allPlayers.filter { player ->
                val currentServer = player.currentServer.orElse(null)
                currentServer?.serverInfo?.name == targetServer
            }
        } else {
            allPlayers
        }

        if (playersToKick.isEmpty()) {
            val message = if (targetServer != null) {
                "<gray>No players found on server <aqua>$targetServer</aqua>."
            } else {
                "<gray>No players are currently online."
            }
            source.sendMessage(miniMessage.deserialize(config.prefix() + message))
            return
        }

        // Kick all players
        val kickMessage = "<red>You have been kicked by an administrator.</red>"
        
        playersToKick.forEach { player ->
            player.disconnect(miniMessage.deserialize(kickMessage))
        }

        // Send confirmation message
        val kickedCount = playersToKick.size
        val confirmationMessage = if (targetServer != null) {
            "<green>Successfully kicked <aqua>$kickedCount</aqua> player(s) from server <aqua>$targetServer</aqua>.</green>"
        } else {
            "<green>Successfully kicked <aqua>$kickedCount</aqua> player(s) from the network.</green>"
        }
        
        source.sendMessage(miniMessage.deserialize(config.prefix() + confirmationMessage))
    }
} 