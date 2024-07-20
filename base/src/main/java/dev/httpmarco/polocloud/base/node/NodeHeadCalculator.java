package dev.httpmarco.polocloud.base.node;

import dev.httpmarco.polocloud.base.Node;
import dev.httpmarco.polocloud.base.node.endpoints.NodeEndpoint;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Comparator;

@UtilityClass
public final class NodeHeadCalculator {

    public boolean isSelfHead() {
        return Node.instance().nodeProvider().externalNodeEndpoints().stream().noneMatch(endpoint -> endpoint.situation() == NodeSituation.REACHABLE);
    }

    public NodeEndpoint calculateNewHead() {
        var collectionOfNodes = new ArrayList<NodeEndpoint>(Node.instance().nodeProvider().externalNodeEndpoints());
        collectionOfNodes.add(Node.instance().nodeProvider().localEndpoint());

        return collectionOfNodes.stream().filter(endpoint -> endpoint.situation() == NodeSituation.REACHABLE).min(Comparator.comparingLong(NodeEndpoint::onlineDuration)).orElse(null);
    }

}
