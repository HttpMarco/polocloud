package dev.httpmarco.polocloud.addons.proxy.platform.waterdog.commands

import dev.waterdog.waterdogpe.command.CommandSender
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.platform.waterdog.WaterdogCloudSubCommand
import dev.httpmarco.polocloud.sdk.java.Polocloud

class ListSubCommand(
    val proxyAddon: ProxyAddon
) : WaterdogCloudSubCommand {

    override fun execute(sender: CommandSender, args: List<String?>) {
        val config = this.proxyAddon.config

        if (!sender.hasPermission("polocloud.addons.proxy.command.cloud.list")) {
            sender.sendMessage(config.prefix() + config.messages("no_permission"))
            return
        }

        try {
            val services = Polocloud.instance().serviceProvider().findAll()

            if (services.isEmpty()) {
                sender.sendMessage(config.prefix() + config.messages("no_server_found"))
                return
            }

            sender.sendMessage(
                buildString {
                    appendLine("")
                    appendLine(config.prefix() + config.messages("services_header"))

                    for (service in services) {
                        val statusColor = when (service.state.name) {
                            "ONLINE" -> "§a"
                            "STARTING" -> "§e"
                            "STOPPING" -> "§c"
                            "STOPPED" -> "§c"
                            else -> "§7"
                        }

                        appendLine(
                            config.prefix() + config.messages("service_info")
                                .replace("%service%", "${service.groupName}-${service.id}")
                                .replace("%status%", "${statusColor}${service.state.name}")
                                .replace("%players%", service.playerCount.toString())
                                .replace("%maxPlayers%", service.maxPlayerCount.toString())
                        )
                    }
                }
            )
        } catch (e: Exception) {
            sender.sendMessage(
                config.prefix() + "§cError while fetching services: ${e.message}"
            )
        }
    }

}