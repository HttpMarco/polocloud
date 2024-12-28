package dev.httpmarco.polocloud.node.group;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.ClusterGroupType;

public class ClusterGroupImpl implements ClusterGroup {

    @Override
    public ClusterGroupType type() {
        return null;
    }

    @Override
    public int minMemory() {
        return 0;
    }

    @Override
    public int maxMemory() {
        return 0;
    }

    @Override
    public String name() {
        return "";
    }
}
