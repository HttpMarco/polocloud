package dev.httpmarco.polocloud.addons.proxy.platform.velocity

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import dev.httpmarco.polocloud.addons.proxy.ProxyConfigAccessor
import dev.httpmarco.polocloud.common.version.polocloudVersion
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.v1.GroupType
import net.kyori.adventure.text.minimessage.MiniMessage
import java.util.concurrent.TimeUnit

class VelocityTablistUpdater (
    velocityPlatform: VelocityPlatform,
    private val server: ProxyServer,
    private val config: ProxyConfigAccessor
) {
    private val miniMessage = MiniMessage.miniMessage()
    private val polocloudVersion = polocloudVersion()

    init {
        server.scheduler.buildTask(velocityPlatform, Runnable {
            updateAll()
        }).repeat(1L, TimeUnit.SECONDS)
            .schedule()
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

        for (player in server.allPlayers) {
            val serverName = player.currentServer.orElse(null)?.serverInfo?.name ?: "unknown"

            val header = formatPlaceholder(configHeader, player, serverName, playerCount, maxPlayers)
            val footer = formatPlaceholder(configFooter, player, serverName, playerCount, maxPlayers)

            player.sendPlayerListHeaderAndFooter(this.miniMessage.deserialize(header), this.miniMessage.deserialize(footer))
        }
    }

    private fun formatPlaceholder(text: String, player: Player, serverName: String, playerCount: Int, maxPlayers: Int): String {
        return text
            .replace("%server%", serverName)
            .replace("%polocloud_version%", polocloudVersion)
            .replace("%player_name%", player.username)
            .replace("%online_players%", playerCount.toString())
            .replace("%max_players%", maxPlayers.toString())
    }
}