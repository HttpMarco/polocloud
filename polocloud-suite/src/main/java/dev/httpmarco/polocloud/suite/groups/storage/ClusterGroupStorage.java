package dev.httpmarco.polocloud.suite.groups.storage;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;

import java.util.List;

public interface ClusterGroupStorage {

    /**
     * Initialize the storage.
     */
    void initialize();

    /**
     * Get a list of all storage files.
     * @return all groups
     */
    List<ClusterGroup> groups();

    /**
     * Update the storage with the given group.
     * @param group the group to update
     */
    void publish(ClusterGroup group);

    /**
     * Get a single group by its identifier.
     * @param identifier the identifier of the group
     * @return the group
     */
    ClusterGroup singleton(String identifier);

    /**
     * Destroy a group by its identifier.
     * @param identifier the identifier of the group
     */
    void destroy(String identifier);

}
