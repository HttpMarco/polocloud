package dev.httpmarco.polocloud.node.cluster.impl;

import dev.httpmarco.osgan.networking.channel.ChannelTransmit;
import dev.httpmarco.osgan.networking.client.CommunicationClient;
import dev.httpmarco.osgan.networking.client.CommunicationClientAction;
import dev.httpmarco.polocloud.api.packet.ConnectionAuthPacket;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.cluster.NodeEndpointData;
import dev.httpmarco.polocloud.node.cluster.NodeSituation;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@Slf4j
@Accessors(fluent = true)
public class ExternalNode extends AbstractNode {

    @Setter
    private @Nullable ChannelTransmit transmit;
    private @Nullable CommunicationClient client;

    public ExternalNode(NodeEndpointData data) {
        super(data);
    }

    @Override
    public ChannelTransmit transmit() {
        return transmit;
    }

    public void connect(Consumer<ChannelTransmit> goodResponse, Consumer<ChannelTransmit> badResponse) {
        this.client = new CommunicationClient(data().hostname(), data().port());
        this.client.clientAction(CommunicationClientAction.FAILED, badResponse);

        this.client.clientAction(CommunicationClientAction.CONNECTED, it -> {
            this.transmit = it;

            // todo wait for response
            transmit.sendPacket(new ConnectionAuthPacket(Node.instance().nodeConfig().clusterToken(), Node.instance().nodeConfig().localNode().name(), ConnectionAuthPacket.Reason.NODE));

            goodResponse.accept(it);
        });

        this.client.clientAction(CommunicationClientAction.DISCONNECTED, it -> {
            transmit = null;

            if(Node.instance().clusterProvider().headNode().data().name().equals(this.data().name())) {
                log.error("Head node disconnected! Search new one...");
            }

            situation(NodeSituation.STOPPED);
            log.info("The node &b{} is disconnected.", data().name());
        });

        this.client.initialize();
    }

    public void close() {
        situation(NodeSituation.STOPPING);

        transmit(null);
        situation(NodeSituation.STOPPED);
    }
}
