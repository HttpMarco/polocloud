package dev.httpmarco.polocloud.addons.proxy.platform.waterdog.events

import dev.waterdog.waterdogpe.ProxyServer
import dev.waterdog.waterdogpe.event.defaults.ProxyPingEvent
import dev.httpmarco.polocloud.addons.proxy.ProxyConfigAccessor
import dev.httpmarco.polocloud.addons.proxy.platform.waterdog.WaterdogPlatform
import dev.httpmarco.polocloud.common.version.polocloudVersion
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.properties.MAINTENANCE

class WaterdogMotdUpdater(
    private val platform: WaterdogPlatform,
    private val config: ProxyConfigAccessor
) {

    private val polocloudVersion: String = polocloudVersion()

    fun onProxyPing(event: ProxyPingEvent) {
        val group = Polocloud.instance().groupProvider()
            .find(platform.proxyAddon().poloService.groupName)
            ?: return

        val proxy = ProxyServer.getInstance()

        val onlinePlayers = proxy.players.values.size

        if (group.properties.get(MAINTENANCE) ?: false) {
            if (!config.maintenanceMotd().enabled) {
                return
            }

            val lineOne = config.maintenanceMotd().lineOne
                .replace("%version%", polocloudVersion)
                .replace("%online_players%", onlinePlayers.toString())

            val lineTwo = config.maintenanceMotd().lineTwo
                .replace("%version%", polocloudVersion)
                .replace("%online_players%", onlinePlayers.toString())

            event.motd = lineOne
            event.subMotd = lineTwo
            return
        }

        if (!config.motd().enabled) {
            return
        }

        val lineOne = config.motd().lineOne
            .replace("%version%", polocloudVersion)
            .replace("%online_players%", onlinePlayers.toString())

        val lineTwo = config.motd().lineTwo
            .replace("%version%", polocloudVersion)
            .replace("%online_players%", onlinePlayers.toString())

        event.motd = lineOne
        event.subMotd = lineTwo
    }
}