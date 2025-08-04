package dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands

import com.velocitypowered.api.command.CommandSource
import dev.httpmarco.polocloud.addons.proxy.CloudSubCommand
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.ProxyConfig
import dev.httpmarco.polocloud.sdk.java.Polocloud
import net.kyori.adventure.text.minimessage.MiniMessage

class StartSubCommand(val proxyAddon: ProxyAddon): CloudSubCommand {

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

        Polocloud.instance().serviceProvider().bootInstance(groupName)
        source.sendMessage(miniMessage.deserialize(config.prefix() + config.messages("starting")))
    }

    private fun usage(config: ProxyConfig): String {
        return buildString {
            appendLine("")
            appendLine(config.prefix() + "<gray>Usage: <aqua>/polocloud start <groupName></aqua>")
            appendLine(config.prefix() + "<gray>Example: <aqua>/polocloud start lobby</aqua>")
        }
    }
}