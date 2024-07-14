package dev.httpmarco.polocloud.base.node;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.node.Node;
import dev.httpmarco.polocloud.api.node.NodeService;
import dev.httpmarco.polocloud.api.packets.nodes.NodeEntryResponsePacket;
import dev.httpmarco.polocloud.base.CloudBase;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.HashSet;

@Getter
@Accessors(fluent = true)
public final class CloudNodeService implements NodeService {

    private final LocalNode localNode;

    private final Cluster cluster;


    public CloudNodeService(LocalNode localNode, Cluster cluster) {
        this.localNode = localNode;
        this.cluster = cluster;

        localNode.initialize();

        localNode.server().responder("cluster-add-endpoint", (property) -> {

            var token = property.getString("token");
            var id = property.getString("id");
            var hostname = property.getString("hostname");
            var port = property.getInteger("port");


            CloudAPI.instance().logger().info("A unknown cluster endpoints trying to connect... (" + id + " " + hostname + ":" + port + ")");

            if (!cluster.token().equals(token)) {
                CloudAPI.instance().logger().info("Failed! Token is invalid.");
                return NodeEntryResponsePacket.fail("The token is invalid");
            }

            if (cluster.hasEndpoint(id, hostname, port)) {
                CloudAPI.instance().logger().info("Failed! Duplicated parameter found.");
                return NodeEntryResponsePacket.fail("Endpoints with this parameter is already registered.");
            }

            CloudAPI.instance().logger().success("The node endpoint " + id + " is now a part of the cluster " + cluster.id());
            cluster.endpoints().add(new Node(id, hostname, port));

            CloudBase.instance().cloudConfiguration().value().cluster(cluster);
            CloudBase.instance().cloudConfiguration().save();

            var sendingNodes = new HashSet<>(cluster.endpoints());
            sendingNodes.add(localNode);

            return NodeEntryResponsePacket.success(sendingNodes, cluster.id());
        });

        localNode.server().responder("cluster-remove-endpoint", property -> {
            // todo
            return NodeEntryResponsePacket.fail("");
        });
    }
}
