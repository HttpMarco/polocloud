package dev.httpmarco.polocloud.base.node.endpoints;

import dev.httpmarco.polocloud.api.cluster.NodeData;
import dev.httpmarco.polocloud.base.node.NodeSituation;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public abstract class NodeEndpoint {

    private final NodeData data;

    private NodeSituation situation;

    @Setter
    private long onlineDuration;

    public NodeEndpoint(NodeData data) {
        this.data = data;

        this.onlineDuration = -1L;
        this.situation = NodeSituation.INITIALIZE;
    }

    public void situation(NodeSituation situation) {
        this.situation = situation;

        if (situation == NodeSituation.REACHABLE) {
            onlineDuration = System.currentTimeMillis();
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NodeEndpoint endpoint && endpoint.data.id().equals(this.data.id());
    }
}
