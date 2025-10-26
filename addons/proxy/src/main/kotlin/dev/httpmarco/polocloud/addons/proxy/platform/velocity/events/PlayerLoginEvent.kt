package dev.httpmarco.polocloud.addons.proxy.platform.velocity.events

import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.ResultedEvent
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.LoginEvent
import com.velocitypowered.api.proxy.ProxyServer
import dev.httpmarco.polocloud.addons.proxy.ProxyConfigAccessor
import dev.httpmarco.polocloud.addons.proxy.platform.velocity.VelocityPlatform
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.properties.MAINTENANCE
import net.kyori.adventure.text.minimessage.MiniMessage

class PlayerLoginEvent(
    private val platform: VelocityPlatform,
    private val server: ProxyServer,
    private val config: ProxyConfigAccessor
) {

    private val miniMessage = MiniMessage.miniMessage()

    @Subscribe(order = PostOrder.FIRST)
    fun onPlayerLogin(event: LoginEvent) {
        val player = event.player
        
        // Check if the server is in maintenance mode
        val group = Polocloud.instance().groupProvider().find(platform.proxyAddon().poloService.groupName) ?: return
        val maintenance = group.properties.get(MAINTENANCE) ?: false

        if (maintenance) {
            // Check if player has bypass permission
            if (!player.hasPermission("polocloud.addons.proxy.maintenance.bypass")) {
                // Kick player from server
                player.disconnect(miniMessage.deserialize(config.messages("maintenance_kick")))
            }
        }
    }
}
