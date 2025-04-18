package dev.httpmarco.polocloud.api;

import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;

public abstract class Polocloud {

    private static Polocloud instance;

    private ClusterGroupProvider groupProvider;

    public Polocloud() {
        instance = this;
    }

    public static Polocloud instance() {
        return instance;
    }

    /**
     * Provides access to the cluster service provider, which manages and interacts with cluster groups.
     *
     * @return an instance of {@code ClusterGroup} representing the cluster group
     */
    public abstract ClusterGroupProvider groupProvider();

    /**
     * Provides access to the service provider that manages and interacts with cluster services.
     *
     * @return an instance of {@code ClusterServiceProvider} for managing cluster services
     */
    public abstract ClusterServiceProvider serviceProvider();
}
