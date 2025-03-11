package dev.httpmarco.polocloud.suite.cluster.global.suites;

import dev.httpmarco.polocloud.suite.cluster.global.ClusterSuite;
import dev.httpmarco.polocloud.suite.cluster.global.ClusterSuiteData;
import dev.httpmarco.polocloud.suite.cluster.grpc.ClusterSuiteGrpcHandler;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.HealthStatusManager;
import java.io.IOException;

public final class LocalSuite implements ClusterSuite {

    private final ClusterSuiteData data;
    private final Server server;

    public LocalSuite(ClusterSuiteData data) {
        this.data = data;
        this.server = ServerBuilder.forPort(data.port()).addService(new ClusterSuiteGrpcHandler()).addService(new HealthStatusManager().getHealthService()).build();

        try {
            this.server.start();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            // todo call shutdown methode
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