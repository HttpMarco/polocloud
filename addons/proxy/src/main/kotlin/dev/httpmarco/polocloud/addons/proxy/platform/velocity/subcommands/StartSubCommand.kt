package dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands

import com.velocitypowered.api.command.CommandSource
import dev.httpmarco.polocloud.addons.proxy.platform.velocity.VelocityCloudSubCommand
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.ProxyConfig
import dev.httpmarco.polocloud.sdk.java.Polocloud
import io.grpc.Status
import io.grpc.StatusRuntimeException
import net.kyori.adventure.text.minimessage.MiniMessage

class StartSubCommand(val proxyAddon: ProxyAddon): VelocityCloudSubCommand {

    private val miniMessage = MiniMessage.miniMessage()

    override fun execute(
        source: CommandSource,
        arguments: List<String>
    ) {
        val config = proxyAddon.config

        if (!source.hasPermission("polocloud.addons.proxy.command.cloud.start")) {
            source.sendMessage(miniMessage.deserialize(config.prefix() + config.messages("no_permission")))
            return
        }

        if (arguments.isEmpty()) {
            source.sendMessage(miniMessage.deserialize(usage(config)))
            return
        }

        val groupName = arguments[0]

        try {
            val service = Polocloud.instance().serviceProvider().bootInstance(groupName)
            source.sendMessage(
                miniMessage.deserialize(
                    config.prefix() + config.messages("starting")
                        .replace("%group%", service.groupName)
                        .replace("%service%", service.groupName + "-" + service.id)
                )
            )
        } catch (e: StatusRuntimeException) {
            if(e.status.code == Status.NOT_FOUND.code) {
                source.sendMessage(
                    miniMessage.deserialize(
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