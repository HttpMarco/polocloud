package dev.httpmarco.polocloud.node.cluster.impl;

import dev.httpmarco.osgan.networking.channel.ChannelTransmit;
import dev.httpmarco.osgan.networking.server.CommunicationServer;
import dev.httpmarco.osgan.networking.server.CommunicationServerAction;
import dev.httpmarco.polocloud.api.packet.resources.services.ServiceConnectPacket;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.cluster.LocalNode;
import dev.httpmarco.polocloud.node.cluster.NodeEndpoint;
import dev.httpmarco.polocloud.node.cluster.NodeEndpointData;
import dev.httpmarco.polocloud.node.cluster.impl.transmit.LocalChannelTransmit;
import dev.httpmarco.polocloud.node.packets.node.NodeConnectPacket;
import dev.httpmarco.polocloud.node.services.ClusterLocalServiceImpl;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Accessors(fluent = true)
public final class LocalNodeImpl extends AbstractNode implements LocalNode {

    private final String hostname;
    private final int port;

    private final CommunicationServer server;
    private final LocalChannelTransmit transmit;

    public LocalNodeImpl(NodeEndpointData data) {
        super(data);

        this.hostname = data.hostname();
        this.port = data.port();

        this.server = new CommunicationServer(hostname, port);

        this.server.clientAction(CommunicationServerAction.CLIENT_DISCONNECT, it -> {
            var possibleService = findLocalService(it);

            if (possibleService != null) {
                // service is unregistered
                return;
            }

            var possibleNode = findChannelNode(it);

            if(possibleNode != null) {

                if(Node.instance().clusterProvider().headNode().equals(possibleNode)) {
                    //todo detect a new head node !!! important
                }

                // this is a node
                possibleNode.close();
                log.info("The Node @&b{} &7disconnected from cluster!", possibleNode.data().name());
            }
        });

        this.server.listen(NodeConnectPacket.class, (it, packet) -> {
            if(!Node.instance().nodeConfig().clusterToken().equals(packet.clusterToken())) {
                it.channel().close();
                return;
            }

            var node = Node.instance().clusterProvider().find(packet.selfId());

            if(node == null) {
                it.channel().close();
                return;
            }

            if(node instanceof ExternalNode externalNode) {
                externalNode.transmit(it);
                log.info("Node @{} connected", externalNode.data().name());
            } else {
                it.channel().close();
            }
        });

        this.server.listen(ServiceConnectPacket.class, (channel, packet) -> {

            if (Node.instance().serviceProvider().services().stream().noneMatch(it -> it.id().equals(packet.id()))) {
                channel.channel().close();
                return;
            }

            var service = Node.instance().serviceProvider().find(packet.id());

            if (service instanceof ClusterLocalServiceImpl localService) {
                localService.transmit(channel);
            } else {
                channel.channel().close();
            }
        });

        this.transmit = new LocalChannelTransmit(server);
    }


    private ClusterLocalServiceImpl findLocalService(ChannelTransmit transmit) {
        return Node.instance().serviceProvider()
                .services()
                .stream()
                .filter(clusterService -> clusterService instanceof ClusterLocalServiceImpl)
                .map(clusterService -> (ClusterLocalServiceImpl) clusterService)
                .filter(localService -> localService.transmit() != null && localService.transmit().equals(transmit))
                .findFirst()
                .orElse(null);
    }

    private ExternalNode findChannelNode(ChannelTransmit transmit) {
        return Node.instance().clusterProvider().endpoints().stream().filter(it -> it.transmit() != null && it.transmit().equals(transmit)).findFirst().map(nodeEndpoint -> (ExternalNode) nodeEndpoint).orElse(null);
    }

    public void initialize() {
        log.info("Node is listening on @{}:{}", hostname, port);
        this.server.initialize();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LocalNodeImpl localNode && localNode.data().name().equals(data().name());
    }

    @Override
    public void close() {
        this.server.close();
        log.info("Node server successfully shutdown.");
    }
}
