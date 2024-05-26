package dev.httpmarco.polocloud.base.services;

import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.osgan.utils.executers.FutureResult;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.packets.CloudAllServicesPacket;
import dev.httpmarco.polocloud.api.packets.CloudServiceRegisterPacket;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.api.services.CloudServiceFactory;
import dev.httpmarco.polocloud.api.services.CloudServiceProvider;
import dev.httpmarco.polocloud.api.services.ServiceFilter;
import dev.httpmarco.polocloud.base.CloudBase;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Accessors(fluent = true)
public final class CloudServiceProviderImpl implements CloudServiceProvider {

    private final List<CloudService> services = new CopyOnWriteArrayList<>();
    private final CloudServiceFactory factory = new CloudServiceFactoryImpl();
    private final CloudServiceQueue queue = new CloudServiceQueue(this);

    public CloudServiceProviderImpl() {

        // send all services back to request
        CloudBase.instance().transmitter().registerResponder("services-all", (channelTransmit, properties) -> new CloudAllServicesPacket(services));

        CloudBase.instance().transmitter().registerResponder("services-filtering", (channelTransmit, properties) -> switch (properties.readObject("filter", ServiceFilter.class)) {
            case EMPTY_SERVICES -> null; //todo
            case PLAYERS_PRESENT_SERVERS -> null; //todo
            case FULL_SERVICES -> null; //todo
            case SAME_NODE_SERVICES -> null; //todo
            case FALLBACKS -> null; //todo
            case PROXIES -> new CloudAllServicesPacket(services.stream().filter(this::isProxy).toList());
            case SERVERS -> new CloudAllServicesPacket(services.stream().filter(it -> !isProxy(it)).toList());
        });

        // allow service to start the process
        queue.start();
    }

    public void close() {
        queue.interrupt();
    }

    public void registerService(CloudService cloudService) {
        this.services.add(cloudService);
    }

    public void unregisterService(CloudService cloudService) {
        this.services.remove(cloudService);
    }

    @Override
    public CompletableFuture<List<CloudService>> servicesAsync() {
        return FutureResult.completedFuture(this.services);
    }

    @Override
    public List<CloudService> filterService(ServiceFilter filter) {
        // todo
        return List.of();
    }

    @Override
    public CompletableFuture<List<CloudService>> filterServiceAsync(ServiceFilter filter) {
        //todo
        return null;
    }

    @Override
    public List<CloudService> services(CloudGroup group) {
        return services.stream().filter(it -> it.group().equals(group)).toList();
    }

    @Override
    public CloudService find(UUID id) {
        return services.stream().filter(it -> it.id().equals(id)).findFirst().orElse(null);
    }

    @Override
    public CloudService service(String name) {
        return services.stream().filter(it -> it.name().equals(name)).findFirst().orElse(null);
    }

    @Override
    public CloudService fromPacket(CloudGroup parent, CodecBuffer buffer) {
        //todo
        return null;
    }

    private boolean isProxy(CloudService service) {
        return CloudBase.instance().groupProvider().platformService().find(service.group().platform()).proxy();
    }
}
