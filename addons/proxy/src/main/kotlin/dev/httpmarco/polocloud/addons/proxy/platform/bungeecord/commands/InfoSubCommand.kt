package dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.commands

import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.BungeecordCloudSubCommand
import dev.httpmarco.polocloud.sdk.java.Polocloud
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import java.time.Duration

class InfoSubCommand(
    val proxyAddon: ProxyAddon
) : BungeecordCloudSubCommand {

    private val polocloudVersion = System.getenv("polocloud-version")?: "unknown"

    override fun execute(sender: CommandSender, args: List<String?>) {
        val config = this.proxyAddon.config

        if (!sender.hasPermission("polocloud.addons.proxy.command.cloud.info")) {
            sender.sendMessage(TextComponent(config.prefix() + config.messages("no_permission")))
            return
        }

        val stats = Polocloud.instance().statsProvider().get()

        sender.sendMessage(
            TextComponent(
                buildString {
                    appendLine("")
                    appendLine(config.prefix() + "§b§lPolocloud Information")
                    appendLine(config.prefix() + "§bVersion: §7${polocloudVersion}")
                    appendLine(config.prefix() + "§bUptime: §7${formatDuration(System.currentTimeMillis() - stats.started)}")
                    appendLine(config.prefix() + "§bRuntime: §7${stats.runtime}")
                    appendLine(config.prefix() + "§bJava Version: §7${stats.javaVersion}")
                    appendLine(config.prefix() + "§bCpu usage: §7${stats.cpuUsage}%")
                    appendLine(config.prefix() + "§bUsed memory: §7${stats.usedMemory}MB")
                    appendLine(config.prefix() + "§bMax memory: §7${stats.maxMemory}MB")
                    appendLine(config.prefix() + "§bSubscribed events: §7${stats.subscribedEvents}")
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