package dev.httpmarco.polocloud.api.services;

import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;

import java.util.List;
import java.util.UUID;

public interface ClusterService extends Named {

    /**
     * Retrieves the unique identifier of the cluster service.
     * For example, 'Lobby-1'. The id is here -1
     *
     * @return the identifier of the cluster service as an integer
     */
    int id();

    /**
     * Retrieves the unique identifier of the cluster service
     *
     * @return the unique identifier of this cluster service as a {@code UUID}
     */
    UUID uniqueId();

    ClusterGroup group();

    ClusterServiceState state();

    boolean executeCommand(String command);

    void shutdown();


    /**
     * Get all log lines of this service, with no limit.
     *
     * @return a list of log lines
     */
    default List<String> logs() {
        return logs(-1);
    }

    /**
     * Get the last {@code lines} log lines of this service.
     *
     * @param lines the number of log lines to retrieve
     * @return a list of log lines
     */
    List<String> logs(int lines);

}
