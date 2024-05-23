package dev.httpmarco.polocloud.base.node;

import dev.httpmarco.polocloud.api.node.NodeService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class CloudNodeService implements NodeService {

    private final LocalNode localNode;
    private final ExternalNode[] externalNodes;

}
