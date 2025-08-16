package dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.commands

import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.BungeecordCloudSubCommand
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent

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

        sender.sendMessage(
            TextComponent(
                buildString {
                    appendLine("")
                    appendLine(config.prefix() + "§b§lPolocloud Information")
                    appendLine(config.prefix() + "§bVersion: §7${polocloudVersion}")
                    appendLine(config.prefix() + "§bUptime: §7TODO")
                    appendLine(config.prefix() + "§bCluster type: §7TODO")
                    appendLine(config.prefix() + "§bJava Version: §7TODO")
                    appendLine(config.prefix() + "§bCpu usage: §7TODO")
                    appendLine(config.prefix() + "§bUsed memory: §7TODO")
                    appendLine(config.prefix() + "§bMax memory: §7TODO")
                    appendLine(config.prefix() + "§bSubscribed events: §7TODO")
                }
            )
        )


    }

}