package dev.httpmarco.polocloud.bridges.fabric.mixin

import net.minecraft.network.ClientConnection
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor
import java.net.InetSocketAddress

@Mixin(ClientConnection::class)
interface ClientConnectionAccessor {

    @set:Accessor("address")
    var address: InetSocketAddress
}