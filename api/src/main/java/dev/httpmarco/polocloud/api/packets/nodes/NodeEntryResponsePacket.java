package dev.httpmarco.polocloud.api.packets.nodes;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.node.Node;
import dev.httpmarco.polocloud.api.packets.ComponentPacketHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public final class NodeEntryResponsePacket extends Packet {

    private boolean successfully;
    private String reason;

    private String clusterId;
    private Set<Node> clusterEndpoints;

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        this.successfully = packetBuffer.readBoolean();

        if (successfully) {
            this.clusterId = packetBuffer.readString();
            var nodeAmount = packetBuffer.readInt();

            this.clusterEndpoints = new HashSet<>();
            for (int i = 0; i < nodeAmount; i++) {
                this.clusterEndpoints.add(ComponentPacketHelper.readNode(packetBuffer));
            }
        } else {
            this.reason = packetBuffer.readString();
        }
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeBoolean(successfully);

        if (successfully) {
            packetBuffer.writeString(clusterId);
            packetBuffer.writeInt(clusterEndpoints.size());
            for (Node endpoint : clusterEndpoints) {
                ComponentPacketHelper.writeNode(endpoint, packetBuffer);
            }
        } else {
            packetBuffer.writeString(reason);
        }
    }


    @Contract("_ -> new")
    public static @NotNull NodeEntryResponsePacket fail(String reason) {
        return new NodeEntryResponsePacket(false, reason, null, null);
    }

    public static @NotNull NodeEntryResponsePacket success(Set<Node> nodes, String clusterId) {
        return new NodeEntryResponsePacket(true, null, clusterId, nodes);
    }
}
