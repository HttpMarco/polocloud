package dev.httpmarco.polocloud.suite.services;

import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;

import java.util.List;

public class ClusterServiceProviderImpl implements ClusterServiceProvider {

    @Override
    public List<ClusterService> findAll() {
        return List.of();
    }
}
