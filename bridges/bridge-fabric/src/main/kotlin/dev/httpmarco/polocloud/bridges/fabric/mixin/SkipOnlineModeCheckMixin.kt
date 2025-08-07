package dev.httpmarco.polocloud.bridges.fabric.mixin

import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerLoginNetworkHandler
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect

@Mixin(ServerLoginNetworkHandler::class)
class SkipOnlineModeCheckMixin {

    @Redirect(method = ["onHello"], at = At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;isOnlineMode()Z"))
    private fun redirectIsOnlineModeCheck(server: MinecraftServer): Boolean {
        return false // Always pretend the server is in offline mode (skip key exchange)
    }
}