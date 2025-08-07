package dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands

import com.velocitypowered.api.command.CommandSource
import dev.httpmarco.polocloud.addons.proxy.CloudSubCommand
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.ProxyConfig
import dev.httpmarco.polocloud.sdk.java.Polocloud
import net.kyori.adventure.text.minimessage.MiniMessage

class ListSubCommand(val proxyAddon: ProxyAddon): CloudSubCommand {

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
                source.sendMessage(
                    miniMessage.deserialize(
                        config.prefix() + "<gray>No services found.</gray>"
                    )
                )
                return
            }

            source.sendMessage(
                miniMessage.deserialize(
                    buildString {
                        appendLine("")
                        appendLine(config.prefix() + "<gray>Found ${services.size} services:</gray>")
                        
                        for (service in services) {
                            val statusColor = when (service.state.name) {
                                "ONLINE" -> "<green>"
                                "STARTING" -> "<yellow>"
                                "STOPPING" -> "<red>"
                                "STOPPED" -> "<red>"
                                else -> "<gray>"
                            }
                            
                            appendLine(
                                config.prefix() + " <gray>-</gray> <aqua>${service.groupName}-${service.id}</aqua> " +
                                "<gray>(</gray>${statusColor}${service.state.name}<gray>)</gray> " +
                                "<gray>Port:</gray> <aqua>${service.port}</aqua> " +
                                "<gray>Players:</gray> <aqua>${service.playerCount}/${service.maxPlayerCount}</aqua>"
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
        return buildString {
            appendLine("")
            appendLine(config.prefix() + "<gray>Usage: <aqua>/polocloud list</aqua>")
            appendLine(config.prefix() + "<gray>Lists all available services and their status.</gray>")
        }
    }
} 