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
public final class NodeValidationSyncPacket extends Packet {

    private boolean successfully;
    private String clusterId;

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        this.successfully = packetBuffer.readBoolean();

        if (successfully) {
            this.clusterId = packetBuffer.readString();
        }
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeBoolean(successfully);

        if (successfully) {
            packetBuffer.writeString(clusterId);
        }
    }
}
