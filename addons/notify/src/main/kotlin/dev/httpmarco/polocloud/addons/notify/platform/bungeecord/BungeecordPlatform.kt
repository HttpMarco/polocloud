package dev.httpmarco.polocloud.addons.notify.platform.bungeecord

import dev.httpmarco.polocloud.addons.notify.NotifyAddon
import dev.httpmarco.polocloud.addons.notify.platform.PlatformNotifyAnnounce
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Plugin

private lateinit var notifyAddon: NotifyAddon

class BungeecordPlatform: Plugin() {

    override fun onEnable() {
        PlatformNotifyAnnounce(alert = {
            ProxyServer.getInstance().players.forEach { player ->
                if (player.hasPermission(it.first)) {
                    player.sendMessage(TextComponent(it.second))
                }
            }
        })
    }
}