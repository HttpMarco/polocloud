package dev.httpmarco.polocloud.node.group;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;

public interface ClusterGroupStorageFactory {

    void store(ClusterGroup group);

    void destroy(ClusterGroup group);

    void update(ClusterGroup group);

    boolean alreadyDefined(ClusterGroup group);

}
