package dev.httpmarco.polocloud.api.packets.nodes;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.node.Node;
import dev.httpmarco.polocloud.api.packets.ComponentPacketHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class NodeBindPacket extends Packet {

    private boolean successfully;
    private String reason;

    private Node node;

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        successfully = packetBuffer.readBoolean();

        if (successfully) {
            this.node = ComponentPacketHelper.readNode(packetBuffer);
        } else {
            this.reason = packetBuffer.readString();
        }
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeBoolean(successfully);
        if (!successfully) {
            packetBuffer.writeString(reason);
        } else {
            ComponentPacketHelper.writeNode(node, packetBuffer);
        }
    }
}
