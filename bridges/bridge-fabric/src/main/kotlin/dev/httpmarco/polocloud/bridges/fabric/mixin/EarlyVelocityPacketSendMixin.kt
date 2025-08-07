package dev.httpmarco.polocloud.bridges.fabric.mixin

import com.mojang.authlib.GameProfile
import dev.httpmarco.polocloud.bridges.fabric.VelocityProtocol
import net.fabricmc.fabric.impl.networking.NetworkHandlerExtensions
import net.fabricmc.fabric.impl.networking.server.ServerLoginNetworkAddon
import net.minecraft.network.ClientConnection
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket
import net.minecraft.server.network.ServerLoginNetworkHandler
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Suppress("UnstableApiUsage")
@Mixin(ServerLoginNetworkHandler::class)
abstract class EarlyVelocityPacketSendMixin {

    @Shadow
    @Final
    protected lateinit var connection: ClientConnection

    @Shadow
    protected var profile: GameProfile? = null

    @Inject(method = ["onHello"], at = [At("HEAD")], cancellable = true)
    private fun injectEarlyVelocityPacket(packet: LoginHelloC2SPacket, ci: CallbackInfo) {
        if (profile != null) {
            return
        }

        val addon = (this as NetworkHandlerExtensions).addon as ServerLoginNetworkAddon
        connection.send(addon.createPacket(VelocityProtocol.PLAYER_INFO_CHANNEL, VelocityProtocol.PLAYER_INFO_PACKET))
        ci.cancel()
    }
}