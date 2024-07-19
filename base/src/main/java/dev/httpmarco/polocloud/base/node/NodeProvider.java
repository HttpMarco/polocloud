package dev.httpmarco.polocloud.base.node;

import dev.httpmarco.polocloud.base.NodeModel;
import dev.httpmarco.polocloud.base.node.endpoints.ExternalNodeEndpoint;
import dev.httpmarco.polocloud.base.node.endpoints.LocalNodeEndpoint;
import dev.httpmarco.polocloud.base.node.endpoints.NodeEndpoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class NodeProvider {

    @Setter
    private @Nullable NodeEndpoint headNodeEndpoint;
    // self node endpoints
    private final LocalNodeEndpoint localEndpoint;
    // all other connected nodes with her data and connection (if present)
    private final Set<ExternalNodeEndpoint> externalNodeEndpoints;

    public NodeProvider(@NotNull NodeModel configuration) {
        this.localEndpoint = new LocalNodeEndpoint(configuration.localNode());
        this.externalNodeEndpoints = configuration.cluster().endpoints().stream().map(ExternalNodeEndpoint::new).collect(Collectors.toSet());
    }

    public void initialize() {
        NodeConnectionFactory.bindCluster(this);
    }

    public boolean isHead() {
        return this.localEndpoint.equals(headNodeEndpoint);
    }
}
