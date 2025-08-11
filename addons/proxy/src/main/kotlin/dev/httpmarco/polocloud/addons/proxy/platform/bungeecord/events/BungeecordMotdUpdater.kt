package dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.events

import dev.httpmarco.polocloud.addons.proxy.ProxyConfigAccessor
import dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.BungeecordPlatform
import dev.httpmarco.polocloud.sdk.java.Polocloud
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.ProxyPingEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class BungeecordMotdUpdater (
    private val platform: BungeecordPlatform,
    private val config: ProxyConfigAccessor
) : Listener {

    private val polocloudVersion: String = System.getenv("polocloud-version") ?: "unknown"

    @EventHandler
    fun onProxyPing(event: ProxyPingEvent) {
        val group = Polocloud.instance().groupProvider().find(platform.proxyAddon().poloService.groupName)!!
        if(group.properties["maintenance"]?.asBoolean ?: false) {
            // maintenance mode is enabled, use maintenance MOTD
            if(!config.maintenanceMotd().enabled) {
                // maintenance motd is disabled
                return
            }
            var motdLines = config.maintenanceMotd().lineOne + "\n" + config.maintenanceMotd().lineTwo
            motdLines = motdLines
                .replace("%version%", polocloudVersion)
            val ping = event.response
            ping.descriptionComponent = TextComponent(motdLines)
            event.response = ping
            return
        }

        if(!config.motd().enabled) {
            // motd module is disabled
            return
        }

        val ping = event.response
        if(ping == null) {
            // no ping data available
            return
        }

        var motdLines = config.motd().lineOne + "\n" + config.motd().lineTwo
        motdLines = motdLines
            .replace("%version%", polocloudVersion)

        ping.descriptionComponent = TextComponent(motdLines)

        event.response = ping

    }

}