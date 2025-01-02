package dev.httpmarco.polocloud.node.group.impl;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.ClusterGroupBuilder;
import dev.httpmarco.polocloud.node.group.ClusterGroupImpl;
import dev.httpmarco.polocloud.node.group.ClusterGroupProviderImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class ClusterGroupBuilderImpl implements ClusterGroupBuilder {

    private final String group;

    private int minMemory;
    private int maxMemory;

    private final ClusterGroupProviderImpl groupProvider;

    @Override
    public ClusterGroupBuilder withMinMemory(int memory) {
        this.minMemory = memory;
        return this;
    }

    @Override
    public ClusterGroupBuilder withMaxMemory(int memory) {
        this.maxMemory = memory;
        return this;
    }

    @Override
    public @NotNull ClusterGroup create() {
        var group = new ClusterGroupImpl(this.group, null, minMemory, maxMemory);

        if(groupProvider.storageFactory().alreadyDefined(group)) {
            // todo better response
            throw new IllegalStateException("Group already defined");
        }

        // save group to the local storage
        groupProvider.storageFactory().store(group);

        // push the group to the sync theme
        this.groupProvider.syncItem().push(group.name(), group);

        return group;
    }
}
