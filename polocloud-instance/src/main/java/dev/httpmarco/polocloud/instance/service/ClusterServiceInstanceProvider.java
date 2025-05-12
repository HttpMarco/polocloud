package dev.httpmarco.polocloud.instance.service;

import dev.httpmarco.polocloud.api.PolocloudEnvironment;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;
import dev.httpmarco.polocloud.explanation.group.ClusterServerServiceGrpc;
import dev.httpmarco.polocloud.explanation.group.ClusterServerServiceOuterClass;
import io.grpc.Channel;

import java.util.List;

public final class ClusterServiceInstanceProvider implements ClusterServiceProvider {

    private final ClusterServerServiceGrpc.ClusterServerServiceBlockingStub stub;

    public ClusterServiceInstanceProvider(Channel channel) {
        this.stub = ClusterServerServiceGrpc.newBlockingStub(channel);
        this.stub.serviceSayOnline(ClusterServerServiceOuterClass.ServiceSayOnlineRequest.newBuilder().setServiceId(System.getenv(PolocloudEnvironment.POLOCLOUD_SERVICE_ID.name())).build());
    }

    @Override
    public List<ClusterService> findAll() {
        return List.of();
    }

    @Override
    public ClusterService find(String name) {
        return null;
    }
}
