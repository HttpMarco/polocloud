package dev.httpmarco.polocloud.base.node.endpoints;

import dev.httpmarco.osgan.networking.channel.ChannelTransmit;
import dev.httpmarco.polocloud.base.node.data.NodeData;
import dev.httpmarco.polocloud.base.node.NodeSituation;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class ExternalNodeEndpoint extends NodeEndpoint {

    @Setter
    private ChannelTransmit transmit;

    public ExternalNodeEndpoint(NodeData data) {
        super(data);
    }

    @Override
    public void situation(NodeSituation situation) {
        super.situation(situation);

        if (situation == NodeSituation.NOT_AVAILABLE) {
            this.transmit = null;
        }
    }
}
