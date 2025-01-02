package dev.httpmarco.polocloud.node.group;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;

import java.util.List;

public interface ClusterGroupStorageFactory {

    void store(ClusterGroup group);

    void destroy(ClusterGroup group);

    void update(ClusterGroup group);

    List<ClusterGroup> searchAll();

    boolean alreadyDefined(ClusterGroup group);

}
