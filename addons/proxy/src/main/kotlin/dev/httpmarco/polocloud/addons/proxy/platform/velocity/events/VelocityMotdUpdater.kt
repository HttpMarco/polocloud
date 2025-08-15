package dev.httpmarco.polocloud.addons.proxy.platform.velocity.events

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyPingEvent
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.server.ServerPing
import dev.httpmarco.polocloud.addons.proxy.ProxyConfigAccessor
import dev.httpmarco.polocloud.addons.proxy.platform.velocity.VelocityPlatform
import dev.httpmarco.polocloud.sdk.java.Polocloud
import net.kyori.adventure.text.minimessage.MiniMessage

class VelocityMotdUpdater (
    private val platform: VelocityPlatform,
    private val server: ProxyServer,
    private val config: ProxyConfigAccessor
) {

    private val polocloudVersion: String = System.getenv("polocloud-version") ?: "unknown"

    @Subscribe
    fun onProxyPing(event: ProxyPingEvent) {
        val group = Polocloud.instance().groupProvider().find(platform.proxyAddon().poloService.groupName)!!
        if(group.properties["maintenance"]?.asBoolean ?: false) {
            // maintenance mode is enabled, use maintenance MOTD
            if(!config.maintenanceMotd().enabled) {
                // maintenance motd is disabled
                return
            }
            val motdLines = config.maintenanceMotd().lineOne + "\n" + config.maintenanceMotd().lineTwo
                .replace("%version%", polocloudVersion)
                .replace("%online_players%", server.playerCount.toString())
            val newVersionName = config.maintenanceMotd().pingMessage
            val newPing = ServerPing.builder()
                .description(MiniMessage.miniMessage().deserialize(motdLines))
                .version(ServerPing.Version(1, newVersionName))
                .maximumPlayers(event.ping.players.orElse(null)?.max ?: 0)
                .onlinePlayers(event.ping.players.orElse(null)?.online ?: 0)

            event.ping = newPing.build()
            return
        }

        if(!config.motd().enabled) {
            // motd module is disabled
            return
        }

        val ping = event.ping
        if(ping == null) {
            // no ping data available
            return
        }

        var motdLines = config.motd().lineOne + "\n" + config.motd().lineTwo
        motdLines = motdLines
            .replace("%version%", polocloudVersion)
            .replace("%online_players%", server.playerCount.toString())

        val newPing = ServerPing.builder()
            .description(MiniMessage.miniMessage().deserialize(motdLines))
            .version(ping.version)
            .maximumPlayers(ping.players.orElse(null) ?.max ?: 0)
            .onlinePlayers(ping.players.orElse(null)?.online ?: 0)

        event.ping = newPing.build()

    }

}