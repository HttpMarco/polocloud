package dev.httpmarco.polocloud.suite.services;

import dev.httpmarco.polocloud.api.services.ClusterService;

import java.nio.file.Path;

public interface ClusterLocalService extends ClusterService {

    int port();

    Path path();

}
