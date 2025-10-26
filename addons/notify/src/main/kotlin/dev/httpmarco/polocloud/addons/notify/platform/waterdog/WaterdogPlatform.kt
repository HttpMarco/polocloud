package dev.httpmarco.polocloud.addons.notify.platform.waterdog

import dev.httpmarco.polocloud.addons.notify.NotifyAddon
import dev.httpmarco.polocloud.addons.notify.platform.PlatformNotifyAnnounce
import dev.waterdog.waterdogpe.ProxyServer
import dev.waterdog.waterdogpe.plugin.Plugin

private lateinit var notifyAddon: NotifyAddon

class WaterdogPlatform: Plugin() {

    override fun onEnable() {
        PlatformNotifyAnnounce(alert = {
            ProxyServer.getInstance().players.values.forEach { player ->
                if (player.hasPermission(it.first)) {
                    player.sendMessage(it.second)
                }
            }
        })
    }
}