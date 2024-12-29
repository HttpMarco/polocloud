package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.polocloud.api.protocol.CloudResultFuture;
import dev.httpmarco.polocloud.api.protocol.CloudResultInfo;
import org.jetbrains.annotations.Nullable;

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
    CloudResultFuture<Collection<ClusterGroup>> findAllAsync();

    /**
     * Get a current group info
     * @param groupId the group id
     * @return the group if present, else null
     */
    default @Nullable ClusterGroup find(String groupId) {
        return findAsync(groupId).now();
    }

    /**
     * Get a current group info async
     * @param groupId the group id
     * @return the group if present, else null
     */
    CloudResultFuture<ClusterGroup> findAsync(String groupId);

    /**
     * Delete an existing group
     * @param group id
     * Return the result of delete processing
     */
    CloudResultInfo delete(String group);

    /**
     * Build a new cluster group
     * @param group the specific id
     * @return group create builder
     */
    ClusterGroupBuilder create(String group);

}