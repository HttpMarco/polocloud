package dev.httpmarco.polocloud.addons.proxy.platform.waterdog.commands

import dev.waterdog.waterdogpe.command.CommandSender
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.platform.waterdog.WaterdogCloudSubCommand
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.properties.MAINTENANCE
import dev.waterdog.waterdogpe.ProxyServer

class MaintenanceSubCommand(
    val proxyAddon: ProxyAddon
) : WaterdogCloudSubCommand {

    override fun execute(sender: CommandSender, args: List<String?>) {
        val config = this.proxyAddon.config

        if (!sender.hasPermission("polocloud.addons.proxy.command.cloud.maintenance")) {
            sender.sendMessage(config.prefix() + config.messages("no_permission"))
            return
        }

        if (args.isEmpty()) {
            sender.sendMessage(config.prefix() + config.messages("usage_maintenance"))
            return
        }

        val action = args[0]?.lowercase() ?: ""

        when (action) {
            "on" -> {

                val group = Polocloud.instance().groupProvider().find(proxyAddon.poloService.groupName)!!
                val maintenanceEnabled = group.properties.get(MAINTENANCE) ?: false
                if(maintenanceEnabled) {
                    sender.sendMessage(config.prefix() + config.messages("maintenance_enabled_already"))
                    return
                }

                group.properties.with(MAINTENANCE, true)
                Polocloud.instance().groupProvider().update(group)

                // Kick all players without bypass permission
                val kickMessage = config.messages("maintenance_kick")
                ProxyServer.getInstance().players.values.forEach { player ->
                    if (!player.hasPermission("polocloud.addons.proxy.maintenance.bypass")) {
                        player.disconnect(kickMessage)
                    }
                }

                sender.sendMessage(config.prefix() + config.messages("maintenance_enabled"))
            }
            "off" -> {

                val group = Polocloud.instance().groupProvider().find(proxyAddon.poloService.groupName)!!
                val maintenanceEnabled = group.properties.get(MAINTENANCE) ?: false
                if(!maintenanceEnabled) {
                    sender.sendMessage(config.prefix() + config.messages("maintenance_disabled_already"))
                    return
                }

                group.properties.with(MAINTENANCE, false)
                Polocloud.instance().groupProvider().update(group)

                sender.sendMessage(config.prefix() + config.messages("maintenance_disabled"))

            }
            else -> {
                sender.sendMessage(config.prefix() + config.messages("usage_maintenance"))
            }
        }
    }

}