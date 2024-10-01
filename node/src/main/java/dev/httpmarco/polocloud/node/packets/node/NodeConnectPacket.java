package dev.httpmarco.polocloud.node.packets.node;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class NodeConnectPacket extends Packet {

    private String clusterToken;
    private String selfId;

    @Override
    public void read(PacketBuffer packetBuffer) {
        this.clusterToken = packetBuffer.readString();
        this.selfId = packetBuffer.readString();
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeString(clusterToken);
        packetBuffer.writeString(selfId);
    }
}
