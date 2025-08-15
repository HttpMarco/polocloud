package dev.httpmarco.polocloud.addons.proxy.platform.velocity.events

import com.velocitypowered.api.event.ResultedEvent
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.LoginEvent
import com.velocitypowered.api.proxy.ProxyServer
import dev.httpmarco.polocloud.addons.api.MiniMessageFormatter
import dev.httpmarco.polocloud.addons.proxy.ProxyConfigAccessor
import dev.httpmarco.polocloud.addons.proxy.platform.velocity.VelocityPlatform
import dev.httpmarco.polocloud.sdk.java.Polocloud
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

class PlayerLoginEvent (
    private val platform: VelocityPlatform,
    private val server: ProxyServer,
    private val config: ProxyConfigAccessor
) {

    @Subscribe
    fun onPlayerLogin(event: LoginEvent) {

        if(!event.player.hasPermission("polocloud.addons.proxy.maintenance.bypass")) {
            // Check if the server is in maintenance mode
            val group = Polocloud.instance().groupProvider().find(platform.proxyAddon().poloService.groupName)!!
            if(group.properties["maintenance"]?.asBoolean ?: false) {
                // Maintenance mode is enabled, check if the maintenance MOTD is enabled
                event.result = ResultedEvent.ComponentResult.denied(MiniMessage.miniMessage().deserialize(config.messages("maintenance_kick")))
                return
            }
        }

    }

}