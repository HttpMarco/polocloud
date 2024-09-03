package dev.httpmarco.polocloud.api.services;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.Sendable;
import dev.httpmarco.polocloud.api.properties.PropertiesBuffer;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public abstract class ClusterServiceProvider implements Sendable<ClusterService> {

    @SneakyThrows
    public List<ClusterService> services() {
        return servicesAsync().get(5, TimeUnit.SECONDS);
    }

    public abstract CompletableFuture<List<ClusterService>> servicesAsync();


    @SneakyThrows
    public ClusterService find(UUID id) {
        return findAsync(id).get(5, TimeUnit.SECONDS);
    }

    public abstract CompletableFuture<ClusterService> findAsync(UUID id);

    @SneakyThrows
    public ClusterService find(String name) {
        return findAsync(name).get(5, TimeUnit.SECONDS);
    }

    public abstract CompletableFuture<ClusterService> findAsync(String name);

    @SneakyThrows
    public List<ClusterService> find(ClusterServiceFilter filter) {
        return findAsync(filter).get(5, TimeUnit.SECONDS);
    }

    public abstract CompletableFuture<List<ClusterService>> findAsync(ClusterServiceFilter filter);

    public abstract ClusterServiceFactory factory();

    @Override
    public void write(@NotNull ClusterService value, @NotNull PacketBuffer buffer) {
        buffer.writeUniqueId(value.id());
        buffer.writeInt(value.orderedId());
        buffer.writeString(value.hostname());
        buffer.writeInt(value.port());
        buffer.writeString(value.runningNode());
        buffer.writeEnum(value.state());

        // we add also all group information
        CloudAPI.instance().groupProvider().write(value.group(), buffer);

        // sync properties
        PropertiesBuffer.write(value.properties(), buffer);
    }
}
