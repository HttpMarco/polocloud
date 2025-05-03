package dev.httpmarco.polocloud.api.services;

import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;

import java.util.UUID;

public interface ClusterService extends Named {

    /**
     * Retrieves the unique identifier of the cluster service.
     * For example, 'Lobby-1'. The id is here -1
     * @return the identifier of the cluster service as an integer
     */
    int id();

    /**
     * Retrieves the unique identifier of the cluster service
     * @return the unique identifier of this cluster service as a {@code UUID}
     */
    UUID uniqueId();

    ClusterGroup group();

    ClusterServiceState state();

    boolean executeCommand(String command);

    void shutdown();

}
