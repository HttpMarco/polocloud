package dev.httpmarco.polocloud.base.node.data;

import dev.httpmarco.polocloud.api.cluster.NodeData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Setter
@Accessors(fluent = true)
@AllArgsConstructor
public final class ClusterData {

    private String id;
    private String token;
    private final Set<NodeData> endpoints;

}
