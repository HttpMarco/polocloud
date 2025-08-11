package dev.httpmarco.polocloud.addons.proxy.platform.velocity

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.ProxyServer
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.ProxyConfig
import dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands.InfoSubCommand
import dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands.ListSubCommand
import dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands.MaintenanceSubCommand
import dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands.PlayersSubCommand
import dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands.StartSubCommand
import dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands.StopSubCommand
import net.kyori.adventure.text.minimessage.MiniMessage

class VelocityCloudCommand(
    val proxyAddon: ProxyAddon,
    private val proxyServer: ProxyServer
) : SimpleCommand {

    private val miniMessage = MiniMessage.miniMessage()
    private val subCommands = mapOf(
        "info" to InfoSubCommand(proxyAddon),
        "start" to StartSubCommand(proxyAddon),
        "stop" to StopSubCommand(proxyAddon),
        "list" to ListSubCommand(proxyAddon),
        "players" to PlayersSubCommand(proxyAddon, proxyServer),
        "maintenance" to MaintenanceSubCommand(proxyAddon)
    )

    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val arguments = invocation.arguments()
        val config = this.proxyAddon.config

        if (!source.hasPermission("polocloud.addons.proxy.command.cloud")) {
            source.sendMessage(miniMessage.deserialize(config.prefix() + config.messages("no_permission")))
            return
        }

        if (arguments.isEmpty()) {
            source.sendMessage(miniMessage.deserialize(usage(config)))
            return
        }

        val subCommandName = arguments[0].lowercase()
        val subCommand = subCommands[subCommandName]

        if (subCommand == null) {
            source.sendMessage(miniMessage.deserialize(usage(config)))
            return
        }

        subCommand.execute(source, arguments.drop(1))
    }

    private fun usage(config: ProxyConfig): String {
        return buildString {
            appendLine("")
            appendLine(config.prefix() + "<gray>Available <gradient:#00fdee:#118bd1><bold>/polocloud</bold></gradient> commands:")
            appendLine(config.prefix() + "<aqua>/polocloud info</aqua>")
            appendLine(config.prefix() + "<aqua>/polocloud list</aqua>")
            appendLine(config.prefix() + "<aqua>/polocloud start <groupName></aqua>")
            appendLine(config.prefix() + "<aqua>/polocloud stop <server></aqua>")
            appendLine(config.prefix() + "<aqua>/polocloud create <template> [static]</aqua>")
            appendLine(config.prefix() + "<aqua>/polocloud delete <server></aqua> <gray>")
            appendLine(config.prefix() + "<aqua>/polocloud maintenance <on|off> [group]</aqua>")
            appendLine(config.prefix() + "<aqua>/polocloud broadcast <message></aqua>")
        }
    }
}