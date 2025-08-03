package dev.httpmarco.polocloud.addons.proxy.platform.bungeecord

import dev.httpmarco.polocloud.addons.proxy.ProxyAddon
import net.md_5.bungee.api.plugin.Plugin
import java.io.File

private lateinit var proxyAddon: ProxyAddon

class BungeecordPlatform: Plugin() {
    override fun onEnable() {
        proxyAddon = ProxyAddon(File("plugins/polocloud"), false)
        //ProxyServer.getInstance().pluginManager.registerCommand(this,BungeecordHubCommand(hubAddon.config))
    }
}