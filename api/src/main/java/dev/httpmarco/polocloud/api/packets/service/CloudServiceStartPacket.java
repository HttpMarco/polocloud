package dev.httpmarco.polocloud.api.packets.service;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class CloudServiceStartPacket extends Packet {

    private String group;

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        this.group = packetBuffer.readString();
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeString(group);
    }
}
