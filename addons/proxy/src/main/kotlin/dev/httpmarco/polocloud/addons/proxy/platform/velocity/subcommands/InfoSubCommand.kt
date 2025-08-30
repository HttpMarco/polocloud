package dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands

import com.velocitypowered.api.command.CommandSource
import dev.httpmarco.polocloud.addons.proxy.platform.velocity.VelocityCloudSubCommand
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.utils.Format
import dev.httpmarco.polocloud.common.version.polocloudVersion
import dev.httpmarco.polocloud.sdk.java.Polocloud
import net.kyori.adventure.text.minimessage.MiniMessage

class InfoSubCommand(val proxyAddon: ProxyAddon): VelocityCloudSubCommand {

    private val miniMessage = MiniMessage.miniMessage()
    private val polocloudVersion = polocloudVersion()

    override fun execute(
        source: CommandSource,
        arguments: List<String>
    ) {
        val config = proxyAddon.config

        if (!source.hasPermission("polocloud.addons.proxy.command.cloud.info")) {
            source.sendMessage(miniMessage.deserialize(config.prefix() + config.messages("no_permission")))
            return
        }

        val stats = Polocloud.instance().cloudInformationProvider().find()

        source.sendMessage(
            miniMessage.deserialize(
                buildString {
                    appendLine("")
                    appendLine(config.prefix() + "<white><st>   </st> PoloCloud Information <st>   </st> </white>")
                    appendLine(config.prefix() + "<gray>Version:</gray> <aqua>${polocloudVersion}</aqua>")
                    appendLine(config.prefix() + "<gray>Uptime:</gray> <aqua>${Format.formatDuration(System.currentTimeMillis() - stats.started)}</aqua>")
                    appendLine(config.prefix() + "<gray>Runtime:</gray> <aqua>${stats.runtime}</aqua>")
                    appendLine(config.prefix() + "<gray>Java Version:</gray> <aqua>${stats.javaVersion}</aqua>")
                    appendLine(config.prefix() + "<gray>Cpu usage:</gray> <aqua>${stats.cpuUsage}%</aqua>")
                    appendLine(config.prefix() + "<gray>Used memory:</gray> <aqua>${stats.usedMemory}MB</aqua>")
                    appendLine(config.prefix() + "<gray>Max memory:</gray> <aqua>${stats.maxMemory}MB</aqua>")
                    appendLine(config.prefix() + "<gray>Subscribed events:</gray> <aqua>${stats.subscribedEvents}</aqua>")
                }
            )
        )
    }

}