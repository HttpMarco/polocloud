package dev.httpmarco.polocloud.suite.cluster.global.suites;

import dev.httpmarco.polocloud.suite.cluster.global.ClusterSuite;
import dev.httpmarco.polocloud.suite.cluster.global.ClusterSuiteData;
import dev.httpmarco.polocloud.suite.cluster.grpc.ClusterSuiteGrpcHandler;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.HealthStatusManager;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Getter
@Accessors(fluent = true)
public final class LocalSuite implements ClusterSuite {

    private static final Logger log = LogManager.getLogger(LocalSuite.class);
    private final ClusterSuiteData data;
    private final Server server;

    public LocalSuite(ClusterSuiteData data) {
        this.data = data;
        this.server = ServerBuilder.forPort(data.port()).addService(new ClusterSuiteGrpcHandler()).addService(new HealthStatusManager().getHealthService()).build();

        try {
            this.server.start();
        } catch (IOException e) {
            log.error("Failed to start local suite server", e);
            System.exit(-1);
        }
    }

    @Override
    public void close() {
        this.server.shutdownNow();
    }

    @Override
    public String id() {
        return data.id();
    }

}