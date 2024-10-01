package dev.httpmarco.polocloud.node.packets.node;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;


@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class NodeHeadRequestPacket extends Packet {

    private String headNode;

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        this.headNode = packetBuffer.readString();
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeString(this.headNode);
    }
}
