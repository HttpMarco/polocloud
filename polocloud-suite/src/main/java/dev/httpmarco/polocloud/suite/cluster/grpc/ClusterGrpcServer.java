package dev.httpmarco.polocloud.suite.cluster.grpc;

import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.global.ClusterSuiteData;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.HealthStatusManager;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;

@Log4j2
@Getter
@Accessors(fluent = true)
public class ClusterGrpcServer implements Closeable {

    private final ClusterSuiteData data;
    private final Server server;

    public ClusterGrpcServer(@NotNull ClusterSuiteData data) {
        this.data = data;
        this.server = ServerBuilder.forPort(data.port()).addService(new ClusterSuiteGrpcHandler()).addService(new HealthStatusManager().getHealthService()).build();

        try {
            this.server.start();
        } catch (IOException e) {
            log.error(PolocloudSuite.instance().translation().get("cluster.grpc.handler.failedToStartLocalSuiteServer"), e);
            System.exit(-1);
        }
    }

    @Override
    public void close() {
        this.server.shutdownNow();
    }
}
