package dev.httpmarco.polocloud.base.node;

import dev.httpmarco.polocloud.base.node.endpoints.ExternalNodeEndpoint;
import dev.httpmarco.polocloud.base.node.endpoints.LocalNodeEndpoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class NodeHeadProvider {

    private LocalNodeEndpoint localEndpoint;
    private ExternalNodeEndpoint[] externalNodeEndpoints;


}
