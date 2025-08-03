package dev.httpmarco.polocloud.addons.proxy.platform.velocity

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.ServerConnectedEvent
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import dev.httpmarco.polocloud.addons.proxy.ProxyConfigAccessor
import dev.httpmarco.polocloud.sdk.java.Polocloud
import net.kyori.adventure.text.minimessage.MiniMessage

class VelocityListener(
    private val config: ProxyConfigAccessor,
    private val server: ProxyServer
) {

    private val miniMessage = MiniMessage.miniMessage()
    private val polocloudVersion = System.getenv("polocloud-version")?: "unknown"

    @Subscribe
    fun onJoin(event: ServerConnectedEvent) {
        val player = event.player
        val server = event.server.serverInfo

        val header = formatPlaceholder(this.config.tablist().header, player, server.name)
        val footer = formatPlaceholder(this.config.tablist().footer, player, server.name)

        player.sendPlayerListHeaderAndFooter(this.miniMessage.deserialize(header), this.miniMessage.deserialize(footer))
    }

    private fun formatPlaceholder(text: String, player: Player, serverName: String): String {
        val service = Polocloud.instance().serviceProvider().find(serverName)
        val name = service?.name() ?: serverName
        val playerCount = service?.playerCount ?: "unknown" // TODO change with hole network
        val maxPlayers = service?.maxPlayerCount ?: "unknown" // TODO change with hole network

        return text
            .replace("%server%", name)
            .replace("%polocloud_version%", polocloudVersion)
            .replace("%player_name%", player.username)
            .replace("%online_players%", playerCount.toString())
            .replace("%max_players%", maxPlayers.toString())
    }
}