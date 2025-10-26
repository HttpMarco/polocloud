package dev.httpmarco.polocloud.addons.proxy.platform.waterdog.commands

import dev.waterdog.waterdogpe.command.CommandSender
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.ProxyConfig
import dev.httpmarco.polocloud.addons.proxy.platform.waterdog.WaterdogCloudSubCommand
import dev.httpmarco.polocloud.sdk.java.Polocloud
import io.grpc.Status
import io.grpc.StatusRuntimeException

class StartSubCommand(
    val proxyAddon: ProxyAddon
) : WaterdogCloudSubCommand {

    override fun execute(sender: CommandSender, args: List<String?>) {
        val config = this.proxyAddon.config

        if (!sender.hasPermission("polocloud.addons.proxy.command.cloud.start")) {
            sender.sendMessage(config.prefix() + config.messages("no_permission"))
            return
        }

        if (args.isEmpty()) {
            sender.sendMessage(usage(config))
            return
        }

        val groupName = args[0] ?: ""

        try {
            val service = Polocloud.instance().serviceProvider().bootInstance(groupName)
            sender.sendMessage(
                config.prefix() + config.messages("starting")
                    .replace("%group%", service.groupName)
                    .replace("%service%", service.groupName + "-" + service.id)
            )
        } catch (e: StatusRuntimeException) {
            if (e.status.code == Status.NOT_FOUND.code) {
                sender.sendMessage(
                    config.prefix() + config.messages("group_not_found")
                        .replace("%group%", groupName)
                )
            }
        }
    }

    private fun usage(config: ProxyConfig): String {
        return config.prefix() + config.messages("usage_start")
    }
}