package dev.httpmarco.polocloud.node.cluster.impl;

import dev.httpmarco.polocloud.node.cluster.NodeEndpoint;
import dev.httpmarco.polocloud.node.cluster.NodeEndpointData;
import dev.httpmarco.polocloud.node.cluster.NodeSituation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Objects;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public abstract class AbstractNode implements NodeEndpoint {

    private final NodeEndpointData data;
    private NodeSituation situation = NodeSituation.INITIALIZE;

    @Override
    public void situation(NodeSituation situation) {
        this.situation = situation;
    }

    @Override
    public void close() {
        Objects.requireNonNull(transmit()).channel().close();
    }
}
