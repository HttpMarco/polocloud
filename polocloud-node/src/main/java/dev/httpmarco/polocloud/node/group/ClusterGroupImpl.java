package dev.httpmarco.polocloud.node.group;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.instance.ClusterGroupServiceBuilder;
import dev.httpmarco.polocloud.node.group.builder.ClusterGroupServiceBuilderImpl;
import dev.httpmarco.polocloud.node.group.impl.ClusterGroupSharedPlatformImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class ClusterGroupImpl implements ClusterGroup {

    private final String name;
    private final ClusterGroupSharedPlatformImpl platform;

    private int minMemory;
    private int maxMemory;

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ClusterGroup group && group.name().equals(name);
    }

    @Contract(" -> new")
    @Override
    public @NotNull ClusterGroupServiceBuilder newInstanceBuilder() {
        return new ClusterGroupServiceBuilderImpl(this);
    }
}
