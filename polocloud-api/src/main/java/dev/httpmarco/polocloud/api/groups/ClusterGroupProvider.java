package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.polocloud.api.protocoll.CloudResultFuture;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface ClusterGroupProvider {

    /**
     * Get all cluster groups
     * @return all groups
     */
    Collection<ClusterGroup> findAll();

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
    @Nullable ClusterGroup find(String groupId);

    /**
     * Get a current group info async
     * @param groupId the group id
     * @return the group if present, else null
     */
    @Nullable ClusterGroup findAsync(String groupId);

    /**
     * Build a new cluster group
     * @param group the specific id
     * @return group create builder
     */
    ClusterGroupBuilder create(String group);

}