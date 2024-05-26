package dev.httpmarco.polocloud.runner.services;

import dev.httpmarco.osgan.files.json.JsonObjectSerializer;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.osgan.utils.executers.FutureResult;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.packets.CloudAllServicesPacket;
import dev.httpmarco.polocloud.api.services.*;
import dev.httpmarco.polocloud.runner.Instance;
import lombok.SneakyThrows;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class InstanceServiceProvider implements CloudServiceProvider {
    @Override
    public CloudServiceFactory factory() {
        return null;
    }

    @Override
    @SneakyThrows
    public List<CloudService> services() {
        return servicesAsync().get(5, TimeUnit.SECONDS);
    }

    @Override
    public CompletableFuture<List<CloudService>> servicesAsync() {
        var future = new FutureResult<List<CloudService>>();
        Instance.instance().client().transmitter().request("services-all", CloudAllServicesPacket.class, it -> future.complete(it.services()));
        return future.toCompletableFuture();
    }

    @Override
    @SneakyThrows
    public List<CloudService> filterService(ServiceFilter filter) {
        return filterServiceAsync(filter).get(5, TimeUnit.SECONDS);
    }

    @Override
    public CompletableFuture<List<CloudService>> filterServiceAsync(ServiceFilter filter) {
        var future = new FutureResult<List<CloudService>>();
        Instance.instance().client().transmitter()
                .request("services-filtering",
                        new JsonObjectSerializer().append("filter", filter),
                        CloudAllServicesPacket.class, it -> future.complete(it.services()));

        return future.toCompletableFuture();
    }

    @Override
    public List<CloudService> services(CloudGroup group) {
        return List.of();
    }

    @Override
    public CloudService find(UUID id) {
        return null;
    }

    @Override
    public CloudService service(String name) {
        return null;
    }

    @Override
    public CloudService fromPacket(CloudGroup parent, CodecBuffer buffer) {
        var orderedId = buffer.readInt();
        var id = buffer.readUniqueId();
        var port = buffer.readInt();
        var state = buffer.readEnum(ServiceState.class);

        return new InstanceCloudService(orderedId, id, port, state, parent);
    }
}
