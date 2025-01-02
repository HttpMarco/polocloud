package dev.httpmarco.polocloud.node.group.builder;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.instance.ClusterGroupServiceBuilder;
import org.jetbrains.annotations.NotNull;

public final class ClusterGroupServiceBuilderImpl extends ClusterGroupServiceBuilder {

    public ClusterGroupServiceBuilderImpl(@NotNull ClusterGroup group) {
        super(group);
    }

    @Override
    public void startService() {
        // todo
    }
}
