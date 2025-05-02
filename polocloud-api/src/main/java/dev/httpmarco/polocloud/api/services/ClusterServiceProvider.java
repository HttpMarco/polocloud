package dev.httpmarco.polocloud.api.services;

import java.util.List;

public interface ClusterServiceProvider {

    List<ClusterService> findAll();

    ClusterService find(String name);

}
