package dev.httpmarco.polocloud.addons.hub.platform

import com.google.inject.Inject
import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import dev.httpmarco.polocloud.addons.hub.HubAddon
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.v1.GroupType
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bstats.velocity.Metrics
import org.slf4j.Logger
import java.io.File

private lateinit var metrics: Metrics

class VelocityPlatform @Inject constructor(
    private val server: ProxyServer,
    private val logger: Logger,
    val metricsFactory: Metrics.Factory
) {

    private lateinit var hubAddon: HubAddon

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        this.hubAddon = HubAddon(File("plugins/polocloud"), true)
        val commandManager = this.server.commandManager
        commandManager.register(
            commandManager.metaBuilder("hub").aliases(*this.hubAddon.config.aliases().toTypedArray()).plugin(this).build(),
            VelocityHubCommand(this.hubAddon, server)
        )

        val pluginId = 26767
        metrics = metricsFactory.make(this, pluginId)
    }
}

class VelocityHubCommand(
    addon: HubAddon,
    private val server: ProxyServer
) : SimpleCommand {

    private val config = addon.config
    private val mm = MiniMessage.miniMessage()

    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()

        if (source !is Player) {
            source.sendMessage(mm.deserialize(config.prefix() + config.messages("only_players")))
            return
        }

        val player = source
        val fallback = Polocloud.instance().serviceProvider()
            .findByType(GroupType.SERVER)
            .firstOrNull {
                it.properties["fallback"]?.equals("true", ignoreCase = true) == true
            }

        if (fallback == null) {
            player.sendMessage(mm.deserialize(config.prefix() + config.messages("no_fallback_found")))
            return
        }

        val serverOpt = server.getServer(fallback.name())

        if (serverOpt.isEmpty) {
            player.sendMessage(mm.deserialize(config.prefix() + config.messages("no_fallback_found")))
            return
        }

        val target = serverOpt.get()

        if (player.currentServer.isPresent && player.currentServer.get().serverInfo.name == target.serverInfo.name) {
            player.sendMessage(
                mm.deserialize(
                    config.prefix() + config.messages("already_connected_to_fallback")
                        .replace("{server}", fallback.name())
                )
            )
            return
        }

        player.createConnectionRequest(target).fireAndForget()
        player.sendMessage(
            mm.deserialize(
                config.prefix() + config.messages("connected_to_fallback")
                    .replace("{server}", fallback.name())
            )
        )
    }
}