package dev.httpmarco.polocloud.api.packet.resources.services;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public abstract class AbstractClusterServiceIdPacket extends Packet {

    private UUID id;

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        this.id = packetBuffer.readUniqueId();
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeUniqueId(id);
    }
}
