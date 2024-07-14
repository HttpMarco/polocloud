package dev.httpmarco.polocloud.base.node.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class ClusterData {

    private final String id;
    private final String token;
    private final Set<NodeData> endpoints;

}
