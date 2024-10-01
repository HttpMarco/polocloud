package dev.httpmarco.polocloud.node.packets.node;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.node.cluster.NodeSituation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class NodeSituationResponsePacket extends Packet {

    private NodeSituation situation;


    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        this.situation = packetBuffer.readEnum(NodeSituation.class);
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeEnum(situation);
    }
}
