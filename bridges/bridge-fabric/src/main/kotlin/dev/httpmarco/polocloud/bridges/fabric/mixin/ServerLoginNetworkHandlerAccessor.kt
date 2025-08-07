package dev.httpmarco.polocloud.bridges.fabric.mixin

import com.mojang.authlib.GameProfile
import net.minecraft.network.ClientConnection
import net.minecraft.server.network.ServerLoginNetworkHandler
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(ServerLoginNetworkHandler::class)
interface ServerLoginNetworkHandlerAccessor {

    @get:Accessor("profile")
    @set:Accessor("profile")
    var profile: GameProfile

    @get:Accessor("connection")
    val connection: ClientConnection
}