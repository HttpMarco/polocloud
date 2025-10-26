package dev.httpmarco.polocloud.addons.hub.platform

import dev.httpmarco.polocloud.addons.hub.HubAddon
import dev.httpmarco.polocloud.addons.hub.HubConfig
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.v1.GroupType
import dev.waterdog.waterdogpe.ProxyServer
import dev.waterdog.waterdogpe.command.Command
import dev.waterdog.waterdogpe.command.CommandSender
import dev.waterdog.waterdogpe.command.CommandSettings
import dev.waterdog.waterdogpe.player.ProxiedPlayer
import dev.waterdog.waterdogpe.plugin.Plugin
import java.io.File

private lateinit var hubAddon: HubAddon

class WaterdogPlatform: Plugin() {

    override fun onEnable() {
        hubAddon = HubAddon(File("plugins/polocloud"), false)
        ProxyServer.getInstance().commandMap.registerCommand(WaterdogHubCommand(hubAddon.config))
    }
}

class WaterdogHubCommand(private val config: HubConfig): Command(
    "hub",
    CommandSettings.builder()
        .setUsageMessage(null)
        .build()
) {

    override fun onExecute(p0: CommandSender?, p1: String?, p2: Array<out String?>?): Boolean {
        if (p0 !is ProxiedPlayer) {
            p0?.sendMessage(config.prefix() + config.messages("only_players"))
            return true
        }

        val fallback = Polocloud.instance().serviceProvider()
            .findByType(GroupType.SERVER)
            .firstOrNull {
                it.properties["fallback"]?.equals("true", ignoreCase = true) == true
            }

        if (fallback == null) {
            p0.sendMessage(config.prefix() + config.messages("no_fallback_found"))
            return true
        }

        val targetServer = ProxyServer.getInstance().getServerInfo(fallback.name())
        if (targetServer == null) {
            p0.sendMessage(config.prefix() + config.messages("no_fallback_found"))
            return true
        }

        if (p0.serverInfo.serverName == targetServer.serverName) {
            p0.sendMessage(
                config.prefix() + config.messages("already_connected_to_fallback")
                    .replace("{server}", fallback.name())
            )
            return true
        }

        p0.connect(targetServer)
        p0.sendMessage(
            config.prefix() + config.messages("connected_to_fallback")
                .replace("{server}", fallback.name())
        )
        return true
    }
}