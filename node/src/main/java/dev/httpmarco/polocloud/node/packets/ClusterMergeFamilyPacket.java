package dev.httpmarco.polocloud.node.packets;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.node.cluster.NodeEndpointData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class ClusterMergeFamilyPacket extends Packet {

    private String clusterId;
    private String clusterToken;
    private Set<NodeEndpointData> allClusterEndpoints;

    @Override
    public void read(PacketBuffer packetBuffer) {
        this.clusterId = packetBuffer.readString();
        this.clusterToken = packetBuffer.readString();

        this.allClusterEndpoints =  new HashSet<>();
        var size = packetBuffer.readInt();
        for (int i = 0; i < size; i++) {
            this.allClusterEndpoints.add(new NodeEndpointData(packetBuffer.readString(), packetBuffer.readString(), packetBuffer.readInt()));
        }
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeString(clusterId);
        packetBuffer.writeString(clusterToken);

        packetBuffer.writeInt(allClusterEndpoints.size());
        for (NodeEndpointData endpoint : allClusterEndpoints) {
            packetBuffer.writeString(endpoint.name());
            packetBuffer.writeString(endpoint.hostname());
            packetBuffer.writeInt(endpoint.port());
        }
    }
}
