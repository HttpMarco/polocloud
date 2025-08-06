package dev.httpmarco.polocloud.addons.notify.platform.bungeecord

import dev.httpmarco.polocloud.addons.notify.NotifyAddon
import net.md_5.bungee.api.plugin.Plugin
import java.io.File

private lateinit var notifyAddon: NotifyAddon

class BungeecordPlatform: Plugin() {
    override fun onEnable() {
        notifyAddon = NotifyAddon(File("plugins/polocloud"), false)
        //ProxyServer.getInstance().pluginManager.registerCommand(this,BungeecordHubCommand(hubAddon.config))
    }
}