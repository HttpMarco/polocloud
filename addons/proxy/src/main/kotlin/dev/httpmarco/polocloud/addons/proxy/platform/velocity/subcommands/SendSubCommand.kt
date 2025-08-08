package dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.server.RegisteredServer
import dev.httpmarco.polocloud.addons.proxy.CloudSubCommand
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import net.kyori.adventure.text.minimessage.MiniMessage

class SendSubCommand(
    private val proxyAddon: ProxyAddon,
    private val proxyServer: ProxyServer
) : CloudSubCommand {

    private val miniMessage = MiniMessage.miniMessage()

    override fun execute(source: CommandSource, arguments: List<String>) {
        val config = this.proxyAddon.config

         if (!source.hasPermission("polocloud.addons.proxy.command.cloud.send")) {
            source.sendMessage(miniMessage.deserialize(config.prefix() + config.messages("no_permission")))
             return
        }

        if (arguments.size < 2) {
            source.sendMessage(miniMessage.deserialize(
                config.prefix() + config.messages("usage_send")
            ))
            return
        }

        val playerName = arguments[0]
        val targetServerName = arguments[1]

        val player = proxyServer.getPlayer(playerName).orElse(null)
        if (player == null) {
            source.sendMessage(miniMessage.deserialize(
                config.prefix() + config.messages("player_not_online")
                    .replace("%player%", playerName)
            ))
            return
        }

        val targetServer = proxyServer.getServer(targetServerName).orElse(null)
        if (targetServer == null) {
            source.sendMessage(miniMessage.deserialize(
                config.prefix() + config.messages("server_not_found")
                    .replace("%server%", targetServerName)
            ))
            return
        }

        val currentServer = player.currentServer.orElse(null)
        if (currentServer?.serverInfo?.name == targetServerName) {
            source.sendMessage(miniMessage.deserialize(
                config.prefix() + config.messages("player_already_on_server")
                    .replace("%player%", playerName)
                    .replace("%server%", targetServerName)
            ))
            return
        }

        player.createConnectionRequest(targetServer).fireAndForget()

        source.sendMessage(miniMessage.deserialize(
            config.prefix() + config.messages("send_success")
                .replace("%player%", playerName)
                .replace("%server%", targetServerName)
        ))
    }
} 