package dev.httpmarco.polocloud.addons.hub.platform

import dev.httpmarco.polocloud.addons.hub.HubAddon
import dev.httpmarco.polocloud.addons.hub.HubConfig
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.v1.GroupType
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Plugin
import org.bstats.bungeecord.Metrics
import java.io.File

private lateinit var hubAddon: HubAddon

class BungeecordPlatform: Plugin() {
    override fun onEnable() {
        hubAddon = HubAddon(File("plugins/polocloud"), false)
        ProxyServer.getInstance().pluginManager.registerCommand(this,BungeecordHubCommand(hubAddon.config))

        Metrics(this, 26766)
    }
}

class BungeecordHubCommand(private val config: HubConfig) : Command("hub", null, *config.aliases().toTypedArray()) {
    override fun execute(sender: CommandSender, args: Array<String>) {
        if(sender !is ProxiedPlayer) {
            sender.sendMessage(TextComponent(config.prefix() +  config.messages("only_players")))
            return
        }

        val fallback = Polocloud.instance().serviceProvider()
            .findByType(GroupType.SERVER)
            .firstOrNull {
                it.properties["fallback"]?.equals("true", ignoreCase = true) == true
            }

        if (fallback == null) {
            sender.sendMessage(TextComponent(config.prefix() + config.messages("no_fallback_found")))
            return
        }

        val targetServer = ProxyServer.getInstance().servers[fallback.name()]
        if (targetServer == null) {
            sender.sendMessage(TextComponent(config.prefix() + config.messages("no_fallback_found")))
            return
        }

        if (sender.server.info.name == targetServer.name) {
            sender.sendMessage(
                TextComponent(

                    config.prefix() + config.messages("already_connected_to_fallback")
                        .replace("{server}", fallback.name())
                )
            )
            return
        }

        sender.connect(targetServer)
        sender.sendMessage(
            TextComponent(
                config.prefix() + config.messages("connected_to_fallback")
                    .replace("{server}", fallback.name())
            )
        )
    }
}