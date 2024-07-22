package dev.httpmarco.polocloud.base.node.packets;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class ClusterHeadNodeResponse extends Packet {

    private String node;

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        this.node = packetBuffer.readString();
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeString(node);
    }
}
