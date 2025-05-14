package dev.httpmarco.polocloud.api.services;

import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;

import java.util.List;
import java.util.UUID;

/**
 * Represents a cluster service within a distributed system, identified by a unique identifier
 * and associated with a specific cluster group. This interface provides methods to retrieve
 * service details, manage its lifecycle, and interact with its processes.
 */
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

    /**
     * Retrieves the {@link ClusterGroup} associated with this cluster service.
     * The cluster group represents a collection of services
     * logically grouped together within the cluster.
     *
     * @return the {@code ClusterGroup} associated with this service
     */
    ClusterGroup group();

    /**
     * Retrieves the current state of the cluster service.
     * The state indicates the lifecycle stage of the service, such as
     * PREPARE, STARTING, ONLINE, STOPPING, or DELETED.
     *
     * @return the current state of the cluster service as a {@code ClusterServiceState}
     */
    ClusterServiceState state();

    /**
     * Executes the specified command on the process associated with the cluster service.
     * This method sends the command to the input stream of the underlying process.
     *
     * @param command the command to execute, represented as a non-null, non-empty {@code String}
     * @return {@code true} if the command was successfully executed; {@code false} if the process is unavailable,
     *         the command is invalid, or an I/O error occurs during execution
     */
    boolean executeCommand(String command);

    /**
     * Shuts down the cluster service, performing necessary cleanup actions.
     * This may interrupt any associated processes or operations related to the service.
     */
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

    /**
     * Constructs the name of the cluster service by combining the group's name
     * and the service's unique identifier, separated by a hyphen.
     *
     * @return the name of the cluster service as a String in the format "group-id"
     */
    @Override
    default String name() {
        return group().name() + "-" + id();
    }

    /**
     * Retrieves the hostname of the cluster service.
     *
     * @return the hostname represented as a {@code String}
     */
    String hostname();

    /**
     * Retrieves the port on which the cluster service is running.
     *
     * @return the port of the service as a {@code String}
     */
    int port();
}
