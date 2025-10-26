package dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ProxyServer
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.platform.velocity.VelocityCloudSubCommand
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.properties.MAINTENANCE
import net.kyori.adventure.text.minimessage.MiniMessage

class MaintenanceSubCommand(val proxyAddon: ProxyAddon, private val proxyServer: ProxyServer) :
        VelocityCloudSubCommand {

    private val miniMessage = MiniMessage.miniMessage()

    override fun execute(source: CommandSource, arguments: List<String>) {
        val config = proxyAddon.config

        if (!source.hasPermission("polocloud.addons.proxy.command.cloud.maintenance")) {
            source.sendMessage(
                    miniMessage.deserialize(config.prefix() + config.messages("no_permission"))
            )
            return
        }

        if (arguments.isEmpty()) {
            source.sendMessage(
                    miniMessage.deserialize(config.prefix() + config.messages("usage_maintenance"))
            )
            return
        }

        val action = arguments[0].lowercase()

        when (action) {
            "on" -> {

                val group =
                        Polocloud.instance()
                                .groupProvider()
                                .find(proxyAddon.poloService.groupName)!!
                val maintenanceEnabled = group.properties.get(MAINTENANCE) ?: false
                if (maintenanceEnabled) {
                    source.sendMessage(
                            miniMessage.deserialize(
                                    config.prefix() + config.messages("maintenance_enabled_already")
                            )
                    )
                    return
                }

                group.properties.with(MAINTENANCE, true)
                Polocloud.instance().groupProvider().update(group)

                // Kick all players without bypass permission
                val kickMessage = miniMessage.deserialize(config.messages("maintenance_kick"))
                proxyServer.allPlayers.forEach { player ->
                    if (!player.hasPermission("polocloud.addons.proxy.maintenance.bypass")) {
                        player.disconnect(kickMessage)
                    }
                }

                source.sendMessage(
                        miniMessage.deserialize(
                                config.prefix() + config.messages("maintenance_enabled")
                        )
                )
            }
            "off" -> {

                val group =
                        Polocloud.instance()
                                .groupProvider()
                                .find(proxyAddon.poloService.groupName)!!
                val maintenanceEnabled = group.properties.get(MAINTENANCE) ?: false
                if (!maintenanceEnabled) {
                    source.sendMessage(
                            miniMessage.deserialize(
                                    config.prefix() +
                                            config.messages("maintenance_disabled_already")
                            )
                    )
                    return
                }

                group.properties.with(MAINTENANCE, false)
                Polocloud.instance().groupProvider().update(group)

                source.sendMessage(
                        miniMessage.deserialize(
                                config.prefix() + config.messages("maintenance_disabled")
                        )
                )
            }
            else -> {
                source.sendMessage(
                        miniMessage.deserialize(
                                config.prefix() + config.messages("usage_maintenance")
                        )
                )
            }
        }
    }
}
