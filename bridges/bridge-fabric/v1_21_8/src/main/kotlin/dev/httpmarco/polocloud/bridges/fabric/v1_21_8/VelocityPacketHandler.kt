package dev.httpmarco.polocloud.bridges.fabric.v1_21_8

import dev.httpmarco.polocloud.bridges.fabric.v1_21_8.FabricBridge.logger
import dev.httpmarco.polocloud.bridges.fabric.v1_21_8.mixin.ClientConnectionAccessor
import dev.httpmarco.polocloud.bridges.fabric.v1_21_8.mixin.ServerLoginNetworkHandlerAccessor
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerLoginNetworkHandler
import net.minecraft.text.Text
import java.net.InetSocketAddress

class VelocityPacketHandler(val secret: String) {

    fun processVelocityLoginPacket(
        server: MinecraftServer,
        handler: ServerLoginNetworkHandler,
        understood: Boolean,
        buf: PacketByteBuf,
        synchronizer: ServerLoginNetworking.LoginSynchronizer,
        ignored: PacketSender
    ) {
        if (!understood) {
            handler.disconnect(Text.of("Please connect using a Velocity proxy"))
            return
        }

        synchronizer.waitFor(server.submit {
            if (!verifyPacketIntegrity(buf)) {
                handler.disconnect(Text.of("Unable to verify player details"))
                return@submit
            }

            try {
                VelocityProtocol.validateForwardingVersion(buf)
            } catch (e: Exception) {
                logger.error("Forwarding version check failed", e)
                handler.disconnect(Text.of("Unable to verify player details"))
                return@submit
            }

            updateConnectionAddress(handler, buf)

            val profile = try {
                VelocityProtocol.readGameProfile(buf)
            } catch (e: Exception) {
                logger.error("Profile creation failed", e)
                handler.disconnect(Text.of("Unable to read player profile"))
                return@submit
            }

            handler.onHello(LoginHelloC2SPacket(profile.name, profile.id))
            (handler as ServerLoginNetworkHandlerAccessor).setProfile(profile)
        })
    }

    private fun verifyPacketIntegrity(buf: PacketByteBuf): Boolean {
        return try {
            VelocityProtocol.verifyPacketIntegrity(buf, secret)
        } catch (e: Throwable) {
            logger.error("Secret check failed", e)
            false
        }
    }

    private fun updateConnectionAddress(handler: ServerLoginNetworkHandler, buf: PacketByteBuf) {
        val connection = (handler as ServerLoginNetworkHandlerAccessor).connection
        val port = (connection.address as InetSocketAddress).port
        val address = VelocityProtocol.readInetAddress(buf)
        (connection as ClientConnectionAccessor).setAddress(InetSocketAddress(address, port))
    }
}