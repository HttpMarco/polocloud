package dev.httpmarco.polocloud.addons.proxy.platform.bungeecord

import dev.httpmarco.polocloud.addons.proxy.ProxyConfigAccessor
import dev.httpmarco.polocloud.common.version.polocloudVersion
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.v1.GroupType
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.concurrent.TimeUnit

class BungeecordTablistUpdater (
    bungeecordPlatform: BungeecordPlatform,
    private val config: ProxyConfigAccessor
) {
    private val polocloudVersion = polocloudVersion()

    init {
        ProxyServer.getInstance().scheduler.schedule(
            bungeecordPlatform,
            this::updateAll,
            1, 1,
            TimeUnit.SECONDS
        )

    }

    private fun updateAll() {
        if( !this.config.tablist().enabled) {
            // tablist module is disabled
            return
        }
        val configHeader = this.config.tablist().header
        val configFooter = this.config.tablist().footer

        val playerCount = Polocloud.instance().playerProvider().playerCount()
        val maxPlayers = Polocloud.instance()
            .serviceProvider()
            .findByType(GroupType.PROXY)
            .sumOf { it.maxPlayerCount }

        for (player in ProxyServer.getInstance().players) {
            val serverName = player.server?.info?.name ?: "unknown"

            val header = formatPlaceholder(configHeader, player, serverName, playerCount, maxPlayers)
            val footer = formatPlaceholder(configFooter, player, serverName, playerCount, maxPlayers)

            player.setTabHeader(TextComponent.fromLegacy(header), TextComponent.fromLegacy(footer))
        }
    }

    private fun formatPlaceholder(text: String, player: ProxiedPlayer, serverName: String, playerCount: Int, maxPlayers: Int): String {
        return text
            .replace("%server%", serverName)
            .replace("%polocloud_version%", polocloudVersion)
            .replace("%player_name%", player.name)
            .replace("%online_players%", playerCount.toString())
            .replace("%max_players%", maxPlayers.toString())
    }
}