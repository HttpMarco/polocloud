package dev.httpmarco.polocloud.node.group;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;

import java.util.List;

public interface ClusterGroupStorageFactory {

    /**
     * Stores a group in the storage
     * @param group the group to store
     */
    void store(ClusterGroup group);

    /**
     * Destroys a group in the storage
     * @param group the group to
     */
    void destroy(ClusterGroup group);

    /**
     * Searches for a group in the storage
     * @return the group
     */
    List<ClusterGroup> searchAll();

    /**
     * Lookup for a group in the storage
     * @param group the group to lookup
     * @return the group
     */
    boolean alreadyDefined(ClusterGroup group);

}
