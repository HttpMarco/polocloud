package dev.httpmarco.polocloud.node.group;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.ClusterGroupType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class ClusterGroupImpl implements ClusterGroup {

    private final String name;
    private final ClusterGroupType type;

    private int minMemory;
    private int maxMemory;

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ClusterGroup group && group.name().equals(name);
    }
}
