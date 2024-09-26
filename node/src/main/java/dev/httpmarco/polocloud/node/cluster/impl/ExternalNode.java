package dev.httpmarco.polocloud.node.cluster.impl;

import dev.httpmarco.osgan.networking.channel.ChannelTransmit;
import dev.httpmarco.polocloud.node.cluster.NodeEndpointData;

public class ExternalNode extends AbstractNode {

    public ExternalNode(NodeEndpointData data) {
        super(data);
    }

    @Override
    public ChannelTransmit transmit() {
        return null;
    }
}
