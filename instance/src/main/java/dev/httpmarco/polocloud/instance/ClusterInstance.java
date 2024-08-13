package dev.httpmarco.polocloud.instance;

import dev.httpmarco.osgan.networking.client.CommunicationClient;
import dev.httpmarco.osgan.networking.client.CommunicationClientAction;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.packet.resources.services.ServiceConnectPacket;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;
import dev.httpmarco.polocloud.instance.events.EventProviderImpl;
import dev.httpmarco.polocloud.instance.groups.ClusterInstanceGroupProvider;
import dev.httpmarco.polocloud.instance.services.ClusterInstanceServiceProvider;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class ClusterInstance extends CloudAPI {

    @Getter
    private static ClusterInstance instance;

    private final UUID selfServiceId = UUID.fromString(System.getenv("serviceId"));
    private ClusterService selfService;

    private final EventProviderImpl eventProvider = new EventProviderImpl();
    private final ClusterInstanceGroupProvider groupProvider = new ClusterInstanceGroupProvider();
    private final ClusterServiceProvider serviceProvider = new ClusterInstanceServiceProvider();
    private final CommunicationClient client;

    @SneakyThrows
    public ClusterInstance(String[] args) {
        instance = this;

        this.client = new CommunicationClient("127.0.0.1", Integer.parseInt(System.getenv("nodeEndPointPort")));
        this.client.initialize();

        this.updateSelfService();

        this.client.clientAction(CommunicationClientAction.CONNECTED, transmit -> {
            transmit.sendPacket(new ServiceConnectPacket(selfServiceId));

            synchronized (this) {
                ClusterInstanceFactory.startPlatform(args);
            }
        });
    }

    public void updateSelfService() {
        serviceProvider().findAsync(selfServiceId).whenComplete((clusterService, throwable) -> this.selfService = clusterService);
    }
}
