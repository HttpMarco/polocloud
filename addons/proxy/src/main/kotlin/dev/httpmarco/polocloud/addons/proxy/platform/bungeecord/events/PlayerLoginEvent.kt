package dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.events

import dev.httpmarco.polocloud.addons.proxy.ProxyConfigAccessor
import dev.httpmarco.polocloud.addons.proxy.platform.bungeecord.BungeecordPlatform
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.properties.MAINTENANCE
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class PlayerLoginEvent (
    private val platform: BungeecordPlatform,
    private val config: ProxyConfigAccessor
) : Listener {

    @EventHandler
    fun onPlayerLogin(event: PostLoginEvent) {

        if(!event.player.hasPermission("polocloud.addons.proxy.maintenance.bypass")) {
            // Check if the server is in maintenance mode
            val group = Polocloud.instance().groupProvider().find(platform.proxyAddon().poloService.groupName)!!
            if(group.properties.get(MAINTENANCE) ?: false) {
                // Maintenance mode is enabled, check if the maintenance MOTD is enabled
                event.player.disconnect(net.md_5.bungee.api.chat.TextComponent(config.messages("maintenance_kick")))
                return
            }
        }

    }
}