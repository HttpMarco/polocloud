package dev.httpmarco.polocloud.base.node.packets;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.base.node.NodeSituation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class NodeSituationCallbackPacket extends Packet {

    private NodeSituation nodeSituation;

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        this.nodeSituation = packetBuffer.readEnum(NodeSituation.class);
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeEnum(nodeSituation);
    }
}
