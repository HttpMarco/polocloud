package dev.httpmarco.polocloud.base.node;

import dev.httpmarco.polocloud.base.Node;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class NodeHeadCalculator {

    public boolean isSelfHead() {
        return Node.instance().nodeProvider().externalNodeEndpoints().stream().noneMatch(endpoint -> endpoint.situation() == NodeSituation.REACHABLE);
    }
}
