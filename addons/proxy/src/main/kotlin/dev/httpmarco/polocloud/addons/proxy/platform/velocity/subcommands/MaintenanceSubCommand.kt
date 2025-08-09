package dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands

import com.google.gson.JsonPrimitive
import com.velocitypowered.api.command.CommandSource
import dev.httpmarco.polocloud.addons.proxy.CloudSubCommand
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.sdk.java.Polocloud
import net.kyori.adventure.text.minimessage.MiniMessage

class MaintenanceSubCommand(val proxyAddon: ProxyAddon): CloudSubCommand {

    private val miniMessage = MiniMessage.miniMessage()

    override fun execute(
        source: CommandSource,
        arguments: List<String>
    ) {
        val config = proxyAddon.config

        if (!source.hasPermission("polocloud.addons.proxy.command.cloud.maintenance")) {
            source.sendMessage(miniMessage.deserialize(config.prefix() + config.messages("no_permission")))
            return
        }

        if (arguments.isEmpty()) {
            source.sendMessage(miniMessage.deserialize(config.prefix() + config.messages("usage_maintenance")))
            return
        }

        val action = arguments[0].lowercase()

        when (action) {
            "on" -> {

                val group = Polocloud.instance().groupProvider().find(proxyAddon.poloService.groupName)!!
                val maintenanceEnabled = group.properties["maintenance"]?.asBoolean ?: false
                if(maintenanceEnabled) {
                    source.sendMessage(miniMessage.deserialize(config.prefix() + config.messages("maintenance_enabled_already")))
                    return
                }

                val properties = HashMap<String, JsonPrimitive>()
                group.properties.forEach { properties[it.key] = it.value }
                properties["maintenance"] = JsonPrimitive(true)
                Polocloud.instance().groupProvider().update(group)

                proxyAddon.poloService = Polocloud.instance().serviceProvider().find(System.getenv("service-name"))!!
                source.sendMessage(miniMessage.deserialize(config.prefix() + config.messages("maintenance_enabled")))

            }
            "off" -> {

                val group = Polocloud.instance().groupProvider().find(proxyAddon.poloService.groupName)!!
                val maintenanceEnabled = group.properties["maintenance"]?.asBoolean ?: false
                if(!maintenanceEnabled) {
                    source.sendMessage(miniMessage.deserialize(config.prefix() + config.messages("maintenance_disabled_already")))
                    return
                }

                val properties = HashMap<String, JsonPrimitive>()
                group.properties.forEach { properties[it.key] = it.value }
                properties["maintenance"] = JsonPrimitive(false)
                Polocloud.instance().groupProvider().update(group)

                proxyAddon.poloService = Polocloud.instance().serviceProvider().find(System.getenv("service-name"))!!
                source.sendMessage(miniMessage.deserialize(config.prefix() + config.messages("maintenance_disabled")))

            }
            else -> {
                source.sendMessage(miniMessage.deserialize(config.prefix() + config.messages("usage_maintenance")))
            }
        }

    }
}