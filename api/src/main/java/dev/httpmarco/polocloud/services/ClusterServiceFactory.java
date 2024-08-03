package dev.httpmarco.polocloud.services;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;

public interface ClusterServiceFactory {

    void runGroupService(ClusterGroup group);

    void shutdownGroupService(ClusterService clusterService);

}
