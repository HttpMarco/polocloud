package dev.httpmarco.polocloud.addons.proxy.platform.velocity.events

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyPingEvent
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.server.ServerPing
import dev.httpmarco.polocloud.addons.proxy.ProxyConfigAccessor
import dev.httpmarco.polocloud.addons.proxy.platform.velocity.VelocityPlatform
import dev.httpmarco.polocloud.common.version.polocloudVersion
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.properties.MAINTENANCE
import net.kyori.adventure.text.minimessage.MiniMessage

class VelocityMotdUpdater(
        private val platform: VelocityPlatform,
        private val server: ProxyServer,
        private val config: ProxyConfigAccessor
) {

    private val polocloudVersion: String = polocloudVersion()

    @Subscribe
    fun onProxyPing(event: ProxyPingEvent) {
        val ping = event.ping

        val group =
                Polocloud.instance()
                        .groupProvider()
                        .find(platform.proxyAddon().poloService.groupName)
                        ?: return
        val maintenance = group.properties.get(MAINTENANCE) ?: false

        if (maintenance) {
            // maintenance mode is enabled, use maintenance MOTD
            if (!config.maintenanceMotd().enabled) {
                // maintenance motd is disabled
                return
            }

            val motdLines =
                    (config.maintenanceMotd().lineOne + "\n" + config.maintenanceMotd().lineTwo)
                            .replace("%version%", polocloudVersion)
                            .replace("%online_players%", server.playerCount.toString())

            val newVersionName = config.maintenanceMotd().pingMessage

            val builder =
                    safeBuilderFrom(ping)
                            .description(MiniMessage.miniMessage().deserialize(motdLines))
                            // keep protocol from original ping, only change displayed name
                            .version(ServerPing.Version(ping.version.protocol, newVersionName))

            // explicitly carry over favicon to avoid it being dropped in some Velocity versions
            ping.favicon.ifPresent { builder.favicon(it) }

            event.ping = builder.build()
            return
        }

        if (!config.motd().enabled) {
            // motd module is disabled
            return
        }

        var motdLines = config.motd().lineOne + "\n" + config.motd().lineTwo
        motdLines =
                motdLines
                        .replace("%version%", polocloudVersion)
                        .replace("%online_players%", server.playerCount.toString())

        val builder =
                safeBuilderFrom(ping)
                        .description(MiniMessage.miniMessage().deserialize(motdLines))
                        // keep original version untouched for non-maintenance
                        .version(ping.version)

        // explicitly preserve favicon
        ping.favicon.ifPresent { builder.favicon(it) }

        event.ping = builder.build()
    }

    private fun safeBuilderFrom(ping: ServerPing): ServerPing.Builder {
        return try {
            ping.asBuilder()
        } catch (_: Throwable) {
            // Fallback for older Velocity versions where asBuilder() might not be available or
            // stable
            ServerPing.builder()
                    .version(ping.version)
                    // carry over known fields to avoid losing data
                    .maximumPlayers(ping.players.orElse(null)?.max ?: 0)
                    .onlinePlayers(ping.players.orElse(null)?.online ?: 0)
                    .apply { ping.favicon.ifPresent { favicon(it) } }
        }
    }
}
