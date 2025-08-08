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

        val allPlayers = proxyServer.allPlayers.toList()
        
        if (allPlayers.isEmpty()) {
            source.sendMessage(miniMessage.deserialize(config.prefix() + config.messages("no_players_online")))
            return
        }

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
                config.messages("no_players_on_server").replace("%server%", targetServer)
            } else {
                config.messages("no_players_online")
            }
            source.sendMessage(miniMessage.deserialize(config.prefix() + message))
            return
        }

        val kickMessage = "<red>You have been kicked by an administrator.</red>"
        
        playersToKick.forEach { player ->
            player.disconnect(miniMessage.deserialize(kickMessage))
        }

        val kickedCount = playersToKick.size
        val confirmationMessage = if (targetServer != null) {
            config.messages("kick_success_server")
                .replace("%count%", kickedCount.toString())
                .replace("%server%", targetServer)
        } else {
            config.messages("kick_success_network")
                .replace("%count%", kickedCount.toString())
        }
        
        source.sendMessage(miniMessage.deserialize(config.prefix() + confirmationMessage))
    }
} 