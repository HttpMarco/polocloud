package dev.httpmarco.polocloud.suite.cluster.suits;

import dev.httpmarco.polocloud.grpc.ClusterSuiteServiceGrpc;
import dev.httpmarco.polocloud.suite.cluster.common.AbstractSuite;
import dev.httpmarco.polocloud.suite.cluster.data.SuiteData;
import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.health.v1.HealthCheckRequest;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.health.v1.HealthGrpc;

public final class ExternalSuite extends AbstractSuite {

    private final ManagedChannel channel;
    private final ClusterSuiteServiceGrpc.ClusterSuiteServiceBlockingStub clusterStub;
    private final HealthGrpc.HealthBlockingStub healthStub;


    public ExternalSuite(SuiteData data) {
        super(data);

        this.channel = ManagedChannelBuilder.forAddress(data().hostname(), data().port()).usePlaintext().build();
        this.clusterStub = ClusterSuiteServiceGrpc.newBlockingStub(channel);
        this.healthStub = HealthGrpc.newBlockingStub(channel);
    }

    @Override
    public void updateInfoSnapshot() {

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
    public void close() {
        this.channel.shutdownNow();
    }

    public ClusterSuiteServiceGrpc.ClusterSuiteServiceBlockingStub clusterStub() {
        return clusterStub;
    }
}
