package dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.commands

import com.google.gson.JsonPrimitive
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.BungeecordCloudSubCommand
import dev.httpmarco.polocloud.sdk.java.Polocloud
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent

class MaintenanceSubCommand(
    val proxyAddon: ProxyAddon
) : BungeecordCloudSubCommand {

    private val polocloudVersion = System.getenv("polocloud-version")?: "unknown"

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
                val maintenanceEnabled = group.properties["maintenance"]?.asBoolean ?: false
                if(maintenanceEnabled) {
                    sender.sendMessage(TextComponent(config.prefix() + config.messages("maintenance_enabled_already")))
                    return
                }

                val properties = HashMap<String, JsonPrimitive>()
                group.properties.forEach {
                    if(it.key != "maintenance") {
                        properties.put(it.key, it.value)
                    }
                }
                properties.put("maintenance", JsonPrimitive(true))
                group.properties = properties
                Polocloud.instance().groupProvider().update(group)

                sender.sendMessage(TextComponent(config.prefix() + config.messages("maintenance_enabled")))

            }
            "off" -> {

                val group = Polocloud.instance().groupProvider().find(proxyAddon.poloService.groupName)!!
                val maintenanceEnabled = group.properties["maintenance"]?.asBoolean ?: false
                if(!maintenanceEnabled) {
                    sender.sendMessage(TextComponent(config.prefix() + config.messages("maintenance_disabled_already")))
                    return
                }

                val properties = HashMap<String, JsonPrimitive>()
                group.properties.forEach {
                    if(it.key != "maintenance") {
                        properties.put(it.key, it.value)
                    }
                }
                group.properties = properties
                Polocloud.instance().groupProvider().update(group)

                sender.sendMessage(TextComponent(config.prefix() + config.messages("maintenance_disabled")))

            }
            else -> {
                sender.sendMessage(TextComponent(config.prefix() + config.messages("usage_maintenance")))
            }
        }

    }

}