package dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.commands

import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.BungeecordCloudSubCommand
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.properties.MAINTENANCE
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent

class MaintenanceSubCommand(
    val proxyAddon: ProxyAddon
) : BungeecordCloudSubCommand {

    override fun execute(sender: CommandSender, args: List<String?>) {
        val config = this.proxyAddon.config

        if (!sender.hasPermission("polocloud.addons.proxy.command.cloud.maintenance")) {
            sender.sendMessage(TextComponent(config.prefix() + config.messages("no_permission")))
            return
        }

        if (args.isEmpty()) {
            sender.sendMessage(TextComponent(config.prefix() + config.messages("usage_maintenance")))
            return
        }

        val action = args[0]?.lowercase() ?: ""

        when (action) {
            "on" -> {

                val group = Polocloud.instance().groupProvider().find(proxyAddon.poloService.groupName)!!
                val maintenanceEnabled = group.properties.get(MAINTENANCE) ?: false
                if(maintenanceEnabled) {
                    sender.sendMessage(TextComponent(config.prefix() + config.messages("maintenance_enabled_already")))
                    return
                }

                group.properties.with(MAINTENANCE, true)
                Polocloud.instance().groupProvider().update(group)

                sender.sendMessage(TextComponent(config.prefix() + config.messages("maintenance_enabled")))

            }
            "off" -> {

                val group = Polocloud.instance().groupProvider().find(proxyAddon.poloService.groupName)!!
                val maintenanceEnabled = group.properties.get(MAINTENANCE) ?: false
                if(!maintenanceEnabled) {
                    sender.sendMessage(TextComponent(config.prefix() + config.messages("maintenance_disabled_already")))
                    return
                }

                group.properties.with(MAINTENANCE, false)
                Polocloud.instance().groupProvider().update(group)

                sender.sendMessage(TextComponent(config.prefix() + config.messages("maintenance_disabled")))

            }
            else -> {
                sender.sendMessage(TextComponent(config.prefix() + config.messages("usage_maintenance")))
            }
        }

    }

}