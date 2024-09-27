package dev.httpmarco.polocloud.node.cluster.impl;

import dev.httpmarco.osgan.networking.channel.ChannelTransmit;
import dev.httpmarco.osgan.networking.client.CommunicationClient;
import dev.httpmarco.osgan.networking.client.CommunicationClientAction;
import dev.httpmarco.polocloud.node.cluster.NodeEndpointData;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ExternalNode extends AbstractNode {

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
            goodResponse.accept(it);
        });
        this.client.initialize();
    }
}
