package dev.httpmarco.polocloud.base.services;

import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.osgan.utils.executers.FutureResult;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.packets.CloudAllServicesPacket;
import dev.httpmarco.polocloud.api.packets.CloudServiceRegisterPacket;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.api.services.CloudServiceFactory;
import dev.httpmarco.polocloud.api.services.CloudServiceProvider;
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
        CloudBase.instance().transmitter().registerResponder("services-all", (channelTransmit, jsonObjectSerializer) -> new CloudAllServicesPacket(services));

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
}
