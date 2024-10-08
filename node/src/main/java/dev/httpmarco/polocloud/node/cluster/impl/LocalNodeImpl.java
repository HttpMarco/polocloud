package dev.httpmarco.polocloud.node.cluster.impl;

import dev.httpmarco.osgan.networking.channel.ChannelTransmit;
import dev.httpmarco.osgan.networking.server.CommunicationServer;
import dev.httpmarco.osgan.networking.server.CommunicationServerAction;
import dev.httpmarco.polocloud.api.packet.ConnectionAuthPacket;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.cluster.LocalNode;
import dev.httpmarco.polocloud.node.cluster.NodeEndpointData;
import dev.httpmarco.polocloud.node.cluster.impl.transmit.LocalChannelTransmit;
import dev.httpmarco.polocloud.node.services.ClusterLocalServiceImpl;
import dev.httpmarco.polocloud.node.util.Address;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Accessors(fluent = true)
public final class LocalNodeImpl extends AbstractNode implements LocalNode {

    private final String hostname;
    private final int port;

    private final String localServiceBindingAddress;

    private final CommunicationServer server;
    private final LocalChannelTransmit transmit;

    public LocalNodeImpl(NodeEndpointData data) {
        super(data);

        this.hostname = data.hostname();
        this.port = data.port();
        this.server = new CommunicationServer(hostname, port);
        this.localServiceBindingAddress = hostname.equals(Address.WILDCARD_ADDRESS) ? Address.LOOPBACK_ADDRESS : hostname;

        // we must be verified the connection, for block unauthorized connections
        this.server.beforePacketHandshake((channel, packet) -> {
            // check if the connection is a node
            if(Node.instance().clusterProvider().isNodeChannel(channel)) {
                return true;
            }

            // check if the connection is a service
            if(Node.instance().serviceProvider().isServiceChannel(channel)) {
                return true;
            }

            // the packet can be use an auth token, for verify the connection
            if(packet instanceof ConnectionAuthPacket authPacket) {

                // confirm the local node token
                if(!authPacket.token().equals(Node.instance().nodeConfig().clusterToken())) {
                    log.warn("Unauthorized cluster token from @{} ", channel.channel().remoteAddress());
                    return false;
                }

                // verify possible service connection
                var possibleService = Node.instance().serviceProvider().find(authPacket.id());
                if(possibleService instanceof ClusterLocalServiceImpl localService) {
                    localService.transmit(channel);
                    return true;
                }

                // verify possible external node connection
                var possibleNode = Node.instance().clusterProvider().find(authPacket.id());
                if(possibleNode instanceof ExternalNode externalNode) {
                    externalNode.transmit(channel);
                    log.info("The Node &8'&7@&8{}&8' &7connected to the cluster&8!", externalNode.data().name());
                    return true;
                }
            }
            log.warn("Unauthorized connection from @{}", packet.getClass());
            return false;
        });

        this.server.clientAction(CommunicationServerAction.CLIENT_DISCONNECT, it -> {
            var possibleService = findLocalService(it);

            if (possibleService != null) {
                // service is unregistered
                return;
            }

            var possibleNode = findChannelNode(it);

            if (possibleNode != null) {

                if (Node.instance().clusterProvider().headNode().equals(possibleNode)) {
                    //todo detect a new head node !!! important
                    log.error("Head node disconnected! Search new one...");
                }

                // this is a node
                possibleNode.close();
                log.info("The Node &8'&7@&b{}' &7disconnected from cluster!", possibleNode.data().name());
            }
        });
        this.transmit = new LocalChannelTransmit(server);
    }

    @Deprecated
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
