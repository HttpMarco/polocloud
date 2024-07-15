package dev.httpmarco.polocloud.base.node.endpoints;

import dev.httpmarco.polocloud.base.node.data.NodeData;
import dev.httpmarco.polocloud.base.node.NodeSituation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public abstract class NodeEndpoint {

    private final NodeData data;

    @Setter
    private NodeSituation situation;

}
