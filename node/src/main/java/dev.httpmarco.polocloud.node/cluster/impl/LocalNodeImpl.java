package dev.httpmarco.polocloud.node.cluster.impl;

import dev.httpmarco.osgan.networking.server.CommunicationServer;
import dev.httpmarco.osgan.networking.server.CommunicationServerAction;
import dev.httpmarco.polocloud.api.packet.resources.services.ServiceConnectPacket;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.cluster.LocalNode;
import dev.httpmarco.polocloud.node.cluster.NodeEndpointData;
import dev.httpmarco.polocloud.node.cluster.impl.transmit.LocalChannelTransmit;
import dev.httpmarco.polocloud.node.services.ClusterLocalServiceImpl;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Accessors(fluent = true)
public final class LocalNodeImpl extends NodeEndpointImpl implements LocalNode {

    private final String hostname = "127.0.0.1";
    private final int port = 9090;

    private final CommunicationServer server;
    private final LocalChannelTransmit transmit;

    public LocalNodeImpl(NodeEndpointData data) {
        super(data);

        this.server = new CommunicationServer(hostname, port);

        this.server.clientAction(CommunicationServerAction.CLIENT_DISCONNECT, it -> {
            //todo set channel of service null
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
