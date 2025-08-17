package dev.httpmarco.polocloud.addons.proxy.platform.velocity.subcommands

import com.velocitypowered.api.command.CommandSource
import dev.httpmarco.polocloud.addons.proxy.platform.velocity.VelocityCloudSubCommand
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.sdk.java.Polocloud
import net.kyori.adventure.text.minimessage.MiniMessage
import java.time.Duration

class InfoSubCommand(val proxyAddon: ProxyAddon): VelocityCloudSubCommand {

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

        val stats = Polocloud.instance().statsProvider().get()

        source.sendMessage(
            miniMessage.deserialize(
                buildString {
                    appendLine("")
                    appendLine(config.prefix() + "<white><st>   </st> PoloCloud Information <st>   </st> </white>")
                    appendLine(config.prefix() + "<gray>Version:</gray> <aqua>${polocloudVersion}</aqua>")
                    appendLine(config.prefix() + "<gray>Uptime:</gray> <aqua>${formatDuration(System.currentTimeMillis() - stats.started)}</aqua>")
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

    fun formatDuration(millis: Long): String {
        var duration = Duration.ofMillis(millis)

        val days = duration.toDays()
        duration = duration.minusDays(days)

        val hours = duration.toHours()
        duration = duration.minusHours(hours)

        val minutes = duration.toMinutes()
        duration = duration.minusMinutes(minutes)

        val seconds = duration.seconds
        val sb = StringBuilder()

        if (days > 0) sb.append(days).append("d ")
        if (hours > 0 || days > 0) sb.append(hours).append("h ")
        if (minutes > 0 || hours > 0 || days > 0) sb.append(minutes).append("m ")
        if (seconds > 0 || minutes > 0 || hours > 0 || days > 0) sb.append(seconds).append("s ")

        return sb.toString()
    }
}