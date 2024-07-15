package dev.httpmarco.polocloud.base.node.endpoints;

import dev.httpmarco.polocloud.base.node.data.NodeData;
import dev.httpmarco.polocloud.base.node.NodeSituation;

public final class ExternalNodeEndpoint extends NodeEndpoint {

    public ExternalNodeEndpoint(NodeData data) {
        super(data, NodeSituation.INITIALIZE);
    }
}
