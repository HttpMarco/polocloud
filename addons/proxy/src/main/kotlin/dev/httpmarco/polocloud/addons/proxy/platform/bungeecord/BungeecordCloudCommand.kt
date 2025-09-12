package dev.httpmarco.polocloud.addons.proxy.platform.bungeecord

import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.ProxyConfig
import dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.commands.InfoSubCommand
import dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.commands.ListSubCommand
import dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.commands.MaintenanceSubCommand
import dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.commands.PlayersSubCommand
import dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.commands.StartSubCommand
import dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.commands.StopSubCommand
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Command

class BungeecordCloudCommand(
    val proxyAddon: ProxyAddon
) : Command("polocloud", "polocloud.addons.proxy.command.cloud", *proxyAddon.config.aliases().toTypedArray()) {

    private val subCommands = mapOf(
        "info" to InfoSubCommand(proxyAddon),
        "start" to StartSubCommand(proxyAddon),
        "stop" to StopSubCommand(proxyAddon),
        "list" to ListSubCommand(proxyAddon),
        "players" to PlayersSubCommand(proxyAddon),
        "maintenance" to MaintenanceSubCommand(proxyAddon)
    )

    override fun execute(p0: CommandSender?, p1: Array<out String?>?) {

        if (p0 == null || p1 == null) {
            return
        }

        val config = this.proxyAddon.config

        if (!p0.hasPermission("polocloud.addons.proxy.command.cloud")) {
            p0.sendMessage(TextComponent(config.prefix() + config.messages("no_permission")))
            return
        }

        if (p1.isEmpty()) {
            p0.sendMessage(TextComponent(usage(config)))
            return
        }

        val subCommandName = p1[0]?.lowercase() ?: return p0.sendMessage(TextComponent(usage(config)))
        val subCommand = subCommands[subCommandName]

        if (subCommand == null) {
            p0.sendMessage(TextComponent(usage(config)))
            return
        }

        subCommand.execute(p0, p1.drop(1))

    }

    private fun usage(config: ProxyConfig): String {
        return buildString {
            appendLine("")
            appendLine(config.prefix() + config.messages("usage_header"))
            appendLine(config.prefix() + config.messages("usage_info"))
            appendLine(config.prefix() + config.messages("usage_list"))
            appendLine(config.prefix() + config.messages("usage_start"))
            appendLine(config.prefix() + config.messages("usage_stop"))
            appendLine(config.prefix() + config.messages("usage_create"))
            appendLine(config.prefix() + config.messages("usage_delete"))
            appendLine(config.prefix() + config.messages("usage_maintenance"))
            appendLine(config.prefix() + config.messages("usage_broadcast"))
        }
    }

}