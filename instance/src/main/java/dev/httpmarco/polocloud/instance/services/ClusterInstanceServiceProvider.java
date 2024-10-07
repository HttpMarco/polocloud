package dev.httpmarco.polocloud.instance.services;

import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.packet.resources.services.ClusterServicePacket;
import dev.httpmarco.polocloud.api.packet.resources.services.ServiceCollectionPacket;
import dev.httpmarco.polocloud.api.properties.PropertiesBuffer;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.api.services.*;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class ClusterInstanceServiceProvider extends ClusterServiceProvider implements ClusterServiceFactory {

    @Contract(pure = true)
    @Override
    public @NotNull CompletableFuture<List<ClusterService>> servicesAsync() {
        var future = new CompletableFuture<List<ClusterService>>();
        ClusterInstance.instance().client().requestAsync("service-all", ServiceCollectionPacket.class).whenComplete((it, t) -> future.complete(it.services()));
        return future;
    }

    @Override
    public @NotNull CompletableFuture<ClusterService> findAsync(UUID id) {
        return findService("id", id);
    }

    @Override
    public @NotNull CompletableFuture<ClusterService> findAsync(String name) {
        return findService("name", name);
    }

    @Override
    public @NotNull CompletableFuture<List<ClusterService>> findAsync(ClusterServiceFilter filter) {
        var future = new CompletableFuture<List<ClusterService>>();
        ClusterInstance.instance().client().requestAsync("service-filtering", ServiceCollectionPacket.class, new CommunicationProperty().set("filter", filter)).whenComplete((it, t) -> future.complete(it.services()));
        return future;
    }

    @Override
    public ClusterServiceFactory factory() {
        return this;
    }

    @Contract("_ -> new")
    @Override
    public @NotNull ClusterService read(@NotNull PacketBuffer buffer) {
        var id = buffer.readUniqueId();
        var orderedId = buffer.readInt();
        var hostname = buffer.readString();
        var port = buffer.readInt();
        var maxPlayers = buffer.readInt();
        var runningNode = buffer.readString();
        var state = buffer.readEnum(ClusterServiceState.class);

        // we add also all group information
        var group = CloudAPI.instance().groupProvider().read(buffer);

        var propertiesPool = new PropertiesPool();
        PropertiesBuffer.read(buffer, propertiesPool);

        return new ClusterInstanceServiceImpl(id, orderedId, port, hostname, runningNode, state, group, propertiesPool, maxPlayers);
    }

    @Override
    public void runGroupService(ClusterGroup group) {

    }

    @Override
    public void shutdownGroupService(ClusterService clusterService) {

    }

    private @NotNull CompletableFuture<ClusterService> findService(String id, Object key) {
        var property = new CommunicationProperty();
        if (key instanceof UUID uuid) {
            property.set(id, uuid);
        } else {
            property.set(id, key.toString());
        }
        var future = new CompletableFuture<ClusterService>();
        ClusterInstance.instance().client().requestAsync("service-find", ClusterServicePacket.class, property).whenComplete((it, t) -> future.complete(it.service()));
        return future;
    }

}
