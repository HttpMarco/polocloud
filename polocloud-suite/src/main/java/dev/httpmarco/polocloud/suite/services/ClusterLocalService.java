package dev.httpmarco.polocloud.suite.services;

import dev.httpmarco.polocloud.api.services.ClusterService;
import io.grpc.Channel;

import java.nio.file.Path;

public interface ClusterLocalService extends ClusterService {

    int port();

    Path path();

    Channel channel();

}
