package dev.httpmarco.polocloud.addons.proxy.platform.waterdog

import dev.waterdog.waterdogpe.command.Command
import dev.waterdog.waterdogpe.command.CommandSender
import dev.waterdog.waterdogpe.command.CommandSettings
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.ProxyConfig
import dev.httpmarco.polocloud.addons.proxy.platform.waterdog.commands.InfoSubCommand
import dev.httpmarco.polocloud.addons.proxy.platform.waterdog.commands.ListSubCommand
import dev.httpmarco.polocloud.addons.proxy.platform.waterdog.commands.MaintenanceSubCommand
import dev.httpmarco.polocloud.addons.proxy.platform.waterdog.commands.PlayersSubCommand
import dev.httpmarco.polocloud.addons.proxy.platform.waterdog.commands.StartSubCommand
import dev.httpmarco.polocloud.addons.proxy.platform.waterdog.commands.StopSubCommand
import kotlin.collections.drop
import kotlin.collections.isEmpty

class WaterdogCloudCommand(
    val proxyAddon: ProxyAddon
): Command(
    "cloud",
    CommandSettings.builder().setUsageMessage(null)
        .build()
) {

    private val subCommands = mapOf(
        "info" to InfoSubCommand(proxyAddon),
        "start" to StartSubCommand(proxyAddon),
        "stop" to StopSubCommand(proxyAddon),
        "list" to ListSubCommand(proxyAddon),
        "players" to PlayersSubCommand(proxyAddon),
        "maintenance" to MaintenanceSubCommand(proxyAddon)
    )

    override fun onExecute(p0: CommandSender?, p1: String?, p2: Array<out String?>?): Boolean {
        if (p0 == null || p2 == null) {
            return true
        }

        val config = this.proxyAddon.config

        if (!p0.hasPermission("polocloud.addons.proxy.command.cloud")) {
            p0.sendMessage(config.prefix() + config.messages("no_permission"))
            return true
        }

        if (p2.isEmpty()) {
            p0.sendMessage(usage(config))
            return true
        }

        val subCommandName = p2[0]?.lowercase() ?: p0.sendMessage(usage(config))
        val subCommand = subCommands[subCommandName]

        if (subCommand == null) {
            p0.sendMessage(usage(config))
            return true
        }

        subCommand.execute(p0, p2.drop(1))
        return true
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