package dev.httpmarco.polocloud.bridges.fabric.v1_21_5

import dev.httpmarco.polocloud.bridges.fabric.config.ConfigLoader
import dev.httpmarco.polocloud.bridges.fabric.config.FabricBridgeConfig
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.LoggerFactory

object FabricBridge : ModInitializer {

    val logger = LoggerFactory.getLogger("FabricBridge")
    val config: FabricBridgeConfig = ConfigLoader.load()

    override fun onInitialize() {
        val version = FabricLoader.getInstance()
            .getModContainer("minecraft")
            .get().metadata.version.friendlyString
        if (version != "1.21.5") {
            return
        }

        ServerLoginNetworking.registerGlobalReceiver(VelocityProtocol.PLAYER_INFO_CHANNEL, VelocityPacketHandler(config.velocitySecret)::processVelocityLoginPacket)
        ServerLoginConnectionEvents.QUERY_START.register { handler, server, sender, synchronizer -> sender.sendPacket(VelocityProtocol.PLAYER_INFO_CHANNEL, VelocityProtocol.PLAYER_INFO_PACKET) }
    }
}
