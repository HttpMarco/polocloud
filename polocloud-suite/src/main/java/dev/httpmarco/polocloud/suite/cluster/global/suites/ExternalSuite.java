package dev.httpmarco.polocloud.suite.cluster.global.suites;

import dev.httpmarco.polocloud.grpc.ClusterService;
import dev.httpmarco.polocloud.grpc.ClusterSuiteServiceGrpc;
import dev.httpmarco.polocloud.suite.cluster.global.ClusterSuite;
import dev.httpmarco.polocloud.suite.cluster.global.ClusterSuiteData;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.health.v1.HealthCheckRequest;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.health.v1.HealthGrpc;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class ExternalSuite implements ClusterSuite {

    private final ClusterSuiteData data;
    private final ManagedChannel channel;

    @Setter
    private ClusterService.State state;

    // stubs
    private final ClusterSuiteServiceGrpc.ClusterSuiteServiceBlockingStub clusterStub;
    private final HealthGrpc.HealthBlockingStub healthStub;

    public ExternalSuite(ClusterSuiteData data) {
        this.data = data;

        this.channel = ManagedChannelBuilder.forAddress(data.hostname(), data.port()).keepAliveWithoutCalls(true).usePlaintext().build();
        this.clusterStub = ClusterSuiteServiceGrpc.newBlockingStub(channel);
        this.healthStub = HealthGrpc.newBlockingStub(channel);
    }

    @Override
    public void close() {
        this.channel.shutdownNow();
    }

    public boolean available() {
        try {
            var request = HealthCheckRequest.newBuilder().setService("").build();
            return healthStub.check(request).getStatus() == HealthCheckResponse.ServingStatus.SERVING;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String id() {
        return data.id();
    }
}