package dev.httpmarco.polocloud.base.node.packets;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.base.node.data.NodeData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class NodeAttachEndpointPacket extends Packet {

    private NodeData nodeData;

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        var id = packetBuffer.readString();
        var hostname = packetBuffer.readString();
        var port = packetBuffer.readInt();

        this.nodeData = new NodeData(id, hostname, port);
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeString(nodeData.id());
        packetBuffer.writeString(nodeData.hostname());
        packetBuffer.writeInt(nodeData.port());
    }
}
