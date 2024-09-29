package dev.httpmarco.polocloud.node.cluster;

import dev.httpmarco.osgan.networking.channel.ChannelTransmit;
import dev.httpmarco.polocloud.api.Closeable;
import org.jetbrains.annotations.Nullable;

public interface NodeEndpoint extends Closeable {

    NodeSituation situation();

    NodeEndpointData data();

    @Nullable ChannelTransmit transmit();

    void situation(NodeSituation situation);

}
