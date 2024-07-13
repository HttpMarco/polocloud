package dev.httpmarco.polocloud.base.node;

import dev.httpmarco.osgan.files.DocumentExclude;
import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.osgan.networking.client.CommunicationClient;
import dev.httpmarco.osgan.networking.server.CommunicationServer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.node.Node;
import dev.httpmarco.polocloud.api.packets.nodes.NodeEntryResponsePacket;
import dev.httpmarco.polocloud.api.packets.service.CloudServiceRegisterPacket;
import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
public class LocalNode extends Node {

    @DocumentExclude
    private CommunicationServer server;

    public LocalNode(String id, String hostname, int port) {
        super(id, hostname, port);
    }

    public void initialize() {
        server = new CommunicationServer(hostname(), port());
        server.initialize();

        server.listen(CloudServiceRegisterPacket.class, (channelTransmit, cloudServiceRegisterPacket) -> {
            var service = CloudAPI.instance().serviceProvider().find(cloudServiceRegisterPacket.uuid());

            if (service instanceof LocalCloudService localCloudService) {
                localCloudService.channelTransmit(channelTransmit);
            } else {
                //todo
            }
        });
    }

    public void close() {
        if (this.server != null) {
            this.server.close();
        }
    }

    public void mergeCluster(@NotNull Node node, String token) {
        var localNode = CloudAPI.instance().nodeService().localNode();
        new CommunicationClient(node.hostname(), node.port(), channelTransmit -> {
            channelTransmit.request("cluster-add-endpoint", new CommunicationProperty()
                    .set("id", localNode.id())
                    .set("hostname", localNode.hostname())
                    .set("port", localNode.port())
                    .set("token", token), NodeEntryResponsePacket.class, packet -> {

                if (packet.successfully()) {

                    var cluster = CloudBase.instance().nodeService().cluster();

                    cluster.id(packet.clusterId());
                    cluster.token(token);
                    cluster.endpoints(packet.clusterEndpoints());

                    CloudAPI.instance().logger().success("Successfully merge into the " + cluster.id() + " cluster!");
                } else {
                    CloudAPI.instance().logger().info("Merge process is failed! Reason: " + packet.reason());
                }
            });
        }).initialize();
    }
}
