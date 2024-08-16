package dev.httpmarco.polocloud.node.groups;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.FallbackClusterGroup;
import org.jetbrains.annotations.NotNull;

public class ClusterGroupFallbackImpl extends ClusterGroupImpl implements FallbackClusterGroup {

    public ClusterGroupFallbackImpl(@NotNull ClusterGroup group) {
        super(group.name(), group.platform(), group.templates(), group.nodes(), group.minMemory(), group.maxMemory(), group.staticService(), group.minOnlineServerInstances(), group.maxOnlineServerInstances());
    }
}
