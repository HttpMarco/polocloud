package dev.httpmarco.polocloud.bridges.fabric

import dev.httpmarco.polocloud.bridges.fabric.config.ConfigLoader
import dev.httpmarco.polocloud.bridges.fabric.config.FabricBridgeConfig
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking
import org.slf4j.LoggerFactory

object FabricBridge : ModInitializer {

    val logger = LoggerFactory.getLogger("FabricBridge")
    val config: FabricBridgeConfig = ConfigLoader.load()

    override fun onInitialize() {
        ServerLoginNetworking.registerGlobalReceiver(VelocityProtocol.PLAYER_INFO_CHANNEL, VelocityPacketHandler(config.velocitySecret)::processVelocityLoginPacket)
        ServerLoginConnectionEvents.QUERY_START.register { handler, server, sender, synchronizer -> sender.sendPacket(VelocityProtocol.PLAYER_INFO_CHANNEL, VelocityProtocol.PLAYER_INFO_PACKET) }
    }
}
