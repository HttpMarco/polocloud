package dev.httpmarco.polocloud.addons.proxy.platform.waterdog.events

import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent
import dev.httpmarco.polocloud.addons.proxy.ProxyConfigAccessor
import dev.httpmarco.polocloud.addons.proxy.platform.waterdog.WaterdogPlatform
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.properties.MAINTENANCE

class PlayerLoginEvent(
    private val platform: WaterdogPlatform,
    private val config: ProxyConfigAccessor
) {

    fun onPlayerLogin(event: PlayerLoginEvent) {
        if (!event.player.hasPermission("polocloud.addons.proxy.maintenance.bypass")) {
            // Check if the server is in maintenance mode
            val group = Polocloud.instance().groupProvider().find(platform.proxyAddon().poloService.groupName)!!
            if (group.properties.get(MAINTENANCE) ?: false) {
                // Check if player has bypass permission
                if (!event.player.hasPermission("polocloud.addons.proxy.maintenance.bypass")) {
                    // Kick player from server
                    event.player.disconnect(config.messages("maintenance_kick"))
                    return
                }
            }
        }
    }
}