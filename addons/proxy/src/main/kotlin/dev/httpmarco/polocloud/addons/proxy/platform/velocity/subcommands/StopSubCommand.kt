package dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands

import com.velocitypowered.api.command.CommandSource
import dev.httpmarco.polocloud.addons.proxy.platform.velocity.VelocityCloudSubCommand
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.ProxyConfig
import dev.httpmarco.polocloud.sdk.java.Polocloud
import io.grpc.Status
import io.grpc.StatusRuntimeException
import net.kyori.adventure.text.minimessage.MiniMessage

class StopSubCommand(val proxyAddon: ProxyAddon): VelocityCloudSubCommand {

    private val miniMessage = MiniMessage.miniMessage()

    override fun execute(
        source: CommandSource,
        arguments: List<String>
    ) {
        val config = proxyAddon.config

        if (!source.hasPermission("polocloud.addons.proxy.command.cloud.stop")) {
            source.sendMessage(miniMessage.deserialize(config.prefix() + config.messages("no_permission")))
            return
        }

        if (arguments.isEmpty()) {
            source.sendMessage(miniMessage.deserialize(usage(config)))
            return
        }

        val serviceName = arguments[0]

        source.sendMessage(miniMessage.deserialize(
            config.prefix() + config.messages("stopping")
                .replace("%service%", serviceName)
        ))
        try {
            val service = Polocloud.instance().serviceProvider().shutdownService(serviceName)
            source.sendMessage(
                miniMessage.deserialize(
                    config.prefix() + config.messages("stopped")
                        .replace("%service%", service.groupName + "-" + service.id)
                        .replace("%group%", service.groupName)
                        .replace("%type%", service.serverType.toString())
                )
            )
        } catch (e: StatusRuntimeException) {
            if(e.status.code == Status.NOT_FOUND.code) {
                source.sendMessage(
                    miniMessage.deserialize(
                        config.prefix() + config.messages("service_not_found")
                            .replace("%service%", serviceName)
                    )
                )
            }
        }
    }

    private fun usage(config: ProxyConfig): String {
        return config.prefix() + config.messages("usage_stop")
    }
}