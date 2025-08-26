package dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.commands

import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.BungeecordCloudSubCommand
import dev.httpmarco.polocloud.sdk.java.Polocloud
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent

class ListSubCommand(
    val proxyAddon: ProxyAddon
) : BungeecordCloudSubCommand {

    override fun execute(sender: CommandSender, args: List<String?>) {
        val config = this.proxyAddon.config

        if (!sender.hasPermission("polocloud.addons.proxy.command.cloud.list")) {
            sender.sendMessage(TextComponent(config.prefix() + config.messages("no_permission")))
            return
        }

        try {
            val services = Polocloud.instance().serviceProvider().findAll()

            if (services.isEmpty()) {
                sender.sendMessage(TextComponent(config.prefix() + config.messages("no_server_found")))
                return
            }

            sender.sendMessage(
                TextComponent(
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
            )
        } catch (e: Exception) {
            sender.sendMessage(
                TextComponent(
                    config.prefix() + "§cError while fetching services: ${e.message}"
                )
            )
        }


    }

}