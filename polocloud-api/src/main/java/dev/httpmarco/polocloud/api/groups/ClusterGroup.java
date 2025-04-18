package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.platform.SharedPlatform;
import dev.httpmarco.polocloud.api.services.ClusterService;

import java.util.List;

public interface ClusterGroup extends Named {

    /**
     * Retrieves the platform information associated with the cluster group.
     *
     * @return the platform information, including platform type, version, and name
     */
    SharedPlatform platform();

    /**
     * Retrieves the minimum memory allocated for a cluster group.
     *
     * @return the minimum memory value in megabytes for this cluster group
     */
    int minMemory();

    /**
     * Retrieves the maximum memory allocated for a cluster group.
     *
     * @return the maximum memory value in megabytes for this cluster group
     */
    int maxMemory();

    /**
     * Retrieves the minimum online service count for a cluster group.
     *
     * @return the minimum number of online services for this cluster group
     */
    int minOnlineService();

    /**
     * Retrieves the maximum online service count for a cluster group.
     *
     * @return the maximum number of online services for this cluster group
     */
    int maxOnlineService();

    /**
     * Retrieves the minimum percentage of online services required to start a new service.
     *
     * @return the percentage of online services required to start a new service
     */
    double percentageToStartNewService();

    /**
     * Retrieves the current number of online services for a cluster group.
     *
     * @return the total number of online services in this cluster group
     */
    int runningServicesAmount();

    /**
     * Retrieves the list of online services associated with this cluster group.
     *
     * @return a list of online services currently active in this cluster group
     */
    List<ClusterService> runningServices();


}
