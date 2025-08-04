package dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands

import com.velocitypowered.api.command.CommandSource
import dev.httpmarco.polocloud.addons.proxy.CloudSubCommand
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import net.kyori.adventure.text.minimessage.MiniMessage

class InfoSubCommand(val proxyAddon: ProxyAddon): CloudSubCommand {

    private val miniMessage = MiniMessage.miniMessage()
    private val polocloudVersion = System.getenv("polocloud-version")?: "unknown"

    override fun execute(
        source: CommandSource,
        arguments: List<String>
    ) {
        val config = proxyAddon.config

        if (!source.hasPermission("polocloud.addons.proxy.command.cloud.info")) {
            source.sendMessage(miniMessage.deserialize(config.prefix() + config.messages("no_permission")))
            return
        }

        source.sendMessage(
            miniMessage.deserialize(
                buildString {
                    appendLine("")
                    appendLine(config.prefix() + "<white><st>   </st> PoloCloud Information <st>   </st> </white>")
                    appendLine(config.prefix() + "<gray>Version:</gray> <aqua>${polocloudVersion}</aqua>")
                    appendLine(config.prefix() + "<gray>Uptime:</gray> <aqua>TODO</aqua>")
                    appendLine(config.prefix() + "<gray>Cluster type:</gray> <aqua>TODO</aqua>")
                    appendLine(config.prefix() + "<gray>Java Version:</gray> <aqua>TODO</aqua>")
                    appendLine(config.prefix() + "<gray>Cpu usage:</gray> <aqua>TODO</aqua>")
                    appendLine(config.prefix() + "<gray>Used memory:</gray> <aqua>TODO</aqua>")
                    appendLine(config.prefix() + "<gray>Max memory:</gray> <aqua>TODO</aqua>")
                    appendLine(config.prefix() + "<gray>Subscribed events:</gray> <aqua>TODO</aqua>")
                }
            )
        )
    }
}