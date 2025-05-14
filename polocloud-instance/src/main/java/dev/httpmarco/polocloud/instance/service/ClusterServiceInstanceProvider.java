package dev.httpmarco.polocloud.instance.service;

import dev.httpmarco.polocloud.api.PolocloudEnvironment;
import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.api.platform.PlatformType;
import dev.httpmarco.polocloud.api.platform.SharedPlatform;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;
import dev.httpmarco.polocloud.api.services.ClusterServiceState;
import dev.httpmarco.polocloud.explanation.group.ClusterServerServiceGrpc;
import dev.httpmarco.polocloud.explanation.group.ClusterServerServiceOuterClass;
import dev.httpmarco.polocloud.instance.group.ClusterInstanceGroup;
import io.grpc.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ClusterServiceInstanceProvider implements ClusterServiceProvider {

    private final ClusterServerServiceGrpc.ClusterServerServiceBlockingStub stub;

    public ClusterServiceInstanceProvider(Channel channel) {
        this.stub = ClusterServerServiceGrpc.newBlockingStub(channel);
        this.stub.serviceSayOnline(ClusterServerServiceOuterClass.ServiceSayOnlineRequest.newBuilder().setServiceId(System.getenv(PolocloudEnvironment.POLOCLOUD_SERVICE_ID.name())).build());
    }

    @Override
    public List<ClusterService> findAll() {
        var response = this.stub.findAllGroup(ClusterServerServiceOuterClass.FindAllServiceRequest.newBuilder().build());
        var services = new ArrayList<ClusterService>();

        for (var service : response.getServicesList()) {
            var group = service.getGroupExplanation();
            var platform = group.getPlatform();

            System.out.println(group.getName());

            services.add(new ClusterServiceInstance(
                    service.getServiceindex(),
                    UUID.fromString(service.getServiceId()),
                    ClusterServiceState.valueOf(service.getState().name()),
                    new ClusterInstanceGroup(group.getName(),
                            new SharedPlatform(platform.getName(), Version.parse(platform.getVersion()), PlatformType.valueOf(platform.getType())),
                            group.getMinMemory(),
                            group.getMaxMemory(),
                            group.getMinOnlineServices(),
                            group.getMaxOnlineServices(),
                            group.getPercentageToStartNewService(),
                            group.getTemplatesList().stream().toList()),
                    service.getHostname(),
                    service.getPort()
            ));
        }
        return services;
    }

    @Override
    public ClusterService find(String name) {
        return null;
    }
}
