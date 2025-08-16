package dev.httpmarco.polocloud.bridges.fabric.v1_21_8.mixin;

import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.net.SocketAddress;

@Mixin(ClientConnection.class)
public interface ClientConnectionAccessor {

    @Accessor("address")
    void setAddress(SocketAddress address);
}
