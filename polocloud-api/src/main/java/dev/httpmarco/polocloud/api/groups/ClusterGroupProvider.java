package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.polocloud.api.utils.Future;

import java.util.Collection;

public interface ClusterGroupProvider {

    /**
     * Get all cluster groups
     * @return all groups
     */
    default Collection<ClusterGroup> findAll(){
        return findAllAsync().now();
    }

    /**
     * Get all cluster groups with an async thread
     * @return all groups
     */
    Future<Collection<ClusterGroup>> findAllAsync();

    /**
     * Get current group info
     * @param groupId the group id
     * @return the group if present, else null
     */
    default ClusterGroup find(String groupId) {
        return findAsync(groupId).now();
    }

    /**
     * Get a current group info async
     * @param groupId the group id
     * @return the group if present, else null
     */
    Future<ClusterGroup> findAsync(String groupId);

    /**
     * Delete an existing group
     * @param group id
     * Return the result of delete processing
     */
    void delete(String group);

    /**
     * Build a new cluster group
     * @param group the specific id
     * @return group create builder
     */
    ClusterGroupBuilder create(String group);

}