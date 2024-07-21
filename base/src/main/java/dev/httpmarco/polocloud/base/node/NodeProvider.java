package dev.httpmarco.polocloud.base.node;

import dev.httpmarco.osgan.networking.channel.ChannelTransmit;
import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.polocloud.base.Node;
import dev.httpmarco.polocloud.base.node.endpoints.ExternalNodeEndpoint;
import dev.httpmarco.polocloud.base.node.endpoints.LocalNodeEndpoint;
import dev.httpmarco.polocloud.base.node.endpoints.NodeEndpoint;
import dev.httpmarco.polocloud.base.node.packets.ClusterEndpointStatePacket;
import dev.httpmarco.polocloud.base.node.packets.ClusterHeadNodeResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class NodeProvider {

    @Setter
    private @Nullable NodeEndpoint headNodeEndpoint;
    // self node endpoints
    private final LocalNodeEndpoint localEndpoint;
    // all other connected nodes with her data and connection (if present)
    private final Set<ExternalNodeEndpoint> externalNodeEndpoints;

    public NodeProvider() {
        var nodeModel = Node.instance().nodeModel();

        this.externalNodeEndpoints = nodeModel.cluster().endpoints().stream().map(ExternalNodeEndpoint::new).collect(Collectors.toSet());

        this.localEndpoint = new LocalNodeEndpoint(nodeModel.localNode());
        this.localEndpoint.server().responder("cluster-node-endpoint-state", property -> new ClusterEndpointStatePacket(localEndpoint.situation(), localEndpoint.onlineDuration()));
        this.localEndpoint.server().responder("cluster-head-node-request", property -> new ClusterHeadNodeResponse(headNodeEndpoint.data().id()));

        /*
        this.localEndpoint.server().responder("cluster-data-sync", property -> {

            if (!property.getString("token").equalsIgnoreCase(nodeModel.cluster().token())) {
                return new NodeValidationSyncPacket(false, null);
            }

            if (!property.getString("bindNode").equalsIgnoreCase(localEndpoint.data().id())) {
                return new NodeValidationSyncPacket(false, null);
            }

            // we must save the new endpoint
            var endpoint = new NodeData(property.getString("id"), property.getString("hostname"), property.getInteger("port"));
            nodeModel.cluster().endpoints().add(endpoint);
            nodeModel.save();

            alertPacket(new NodeAttachEndpointPacket(endpoint));

            Node.instance().logger().success("Successfully bind a new node endpoint in the cluster.");
            return new NodeValidationSyncPacket(true, nodeModel.cluster().id());
        });

        this.localEndpoint.server().listen(NodeAttachEndpointPacket.class, (channelTransmit, nodeAttachEndpointPacket) -> {
            nodeModel.cluster().endpoints().add(nodeAttachEndpointPacket.nodeData());
            nodeModel.save();
        });

         */
    }

    public boolean isHead() {
        return this.localEndpoint.equals(headNodeEndpoint);
    }

    public NodeEndpoint node(String id) {
        if (localEndpoint.data().id().equals(id)) {
            return localEndpoint;
        }
        return this.externalNodeEndpoints.stream().filter(it -> it.data().id().equals(id)).findFirst().orElse(null);
    }

    public ExternalNodeEndpoint node(ChannelTransmit transmit) {
        return this.externalNodeEndpoints.stream().filter(it -> it.transmit() != null && it.transmit().equals(transmit)).findFirst().orElse(null);
    }

    public void alertPacket(Packet packet) {
        for (var externalNodeEndpoint : externalNodeEndpoints) {
            if (externalNodeEndpoint.transmit() == null) {
                return;
            }
            externalNodeEndpoint.transmit().sendPacket(packet);
        }
    }
}