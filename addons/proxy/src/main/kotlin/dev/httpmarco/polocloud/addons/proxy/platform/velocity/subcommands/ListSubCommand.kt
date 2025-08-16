package dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands

import com.velocitypowered.api.command.CommandSource
import dev.httpmarco.polocloud.addons.proxy.platform.velocity.VelocityCloudSubCommand
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.ProxyConfig
import dev.httpmarco.polocloud.sdk.java.Polocloud
import net.kyori.adventure.text.minimessage.MiniMessage

class ListSubCommand(val proxyAddon: ProxyAddon): VelocityCloudSubCommand {

    private val miniMessage = MiniMessage.miniMessage()

    override fun execute(
        source: CommandSource,
        arguments: List<String>
    ) {
        val config = proxyAddon.config

        if (!source.hasPermission("polocloud.addons.proxy.command.cloud.list")) {
            source.sendMessage(miniMessage.deserialize(config.prefix() + config.messages("no_permission")))
            return
        }

        try {
            val services = Polocloud.instance().serviceProvider().findAll()
            
            if (services.isEmpty()) {
                source.sendMessage(miniMessage.deserialize(config.prefix() + config.messages("no_server_found")))
                return
            }

            source.sendMessage(
                miniMessage.deserialize(
                    buildString {
                        appendLine("")
                        appendLine(config.prefix() + config.messages("services_header"))
                        
                        for (service in services) {
                            val statusColor = when (service.state.name) {
                                "ONLINE" -> "<green>"
                                "STARTING" -> "<yellow>"
                                "STOPPING" -> "<red>"
                                "STOPPED" -> "<red>"
                                else -> "<gray>"
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
            source.sendMessage(
                miniMessage.deserialize(
                    config.prefix() + "<red>Error while fetching services: ${e.message}</red>"
                )
            )
        }
    }

    private fun usage(config: ProxyConfig): String {
        return config.prefix() + config.messages("usage_list")
    }
} 