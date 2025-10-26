package dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.commands

import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.ProxyConfig
import dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.BungeecordCloudSubCommand
import dev.httpmarco.polocloud.sdk.java.Polocloud
import io.grpc.Status
import io.grpc.StatusRuntimeException
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent

class StartSubCommand(
    val proxyAddon: ProxyAddon
) : BungeecordCloudSubCommand {

    override fun execute(sender: CommandSender, args: List<String?>) {
        val config = this.proxyAddon.config

        if (!sender.hasPermission("polocloud.addons.proxy.command.cloud.start")) {
            sender.sendMessage(TextComponent(config.prefix() + config.messages("no_permission")))
            return
        }

        if (args.isEmpty()) {
            sender.sendMessage(TextComponent(usage(config)))
            return
        }

        val groupName = args[0]?: ""

        try {
            val service = Polocloud.instance().serviceProvider().bootInstance(groupName)
            sender.sendMessage(
                TextComponent(
                    config.prefix() + config.messages("starting")
                        .replace("%group%", service.groupName)
                        .replace("%service%", service.groupName + "-" + service.id)
                )
            )
        } catch (e: StatusRuntimeException) {
            if(e.status.code == Status.NOT_FOUND.code) {
                sender.sendMessage(
                    TextComponent(
                        config.prefix() + config.messages("group_not_found")
                            .replace("%group%", groupName)
                    )
                )
            }
        }


    }

    private fun usage(config: ProxyConfig): String {
        return config.prefix() + config.messages("usage_start")
    }

}