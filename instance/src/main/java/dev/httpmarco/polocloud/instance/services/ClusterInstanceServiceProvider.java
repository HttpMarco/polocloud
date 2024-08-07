package dev.httpmarco.polocloud.instance.services;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceFactory;
import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ClusterInstanceServiceProvider extends ClusterServiceProvider {

    @Override
    public CompletableFuture<List<ClusterService>> servicesAsync() {
        return null;
    }

    @Override
    public CompletableFuture<ClusterService> findAsync(UUID id) {
        return null;
    }

    @Override
    public CompletableFuture<ClusterService> findAsync(String name) {
        return null;
    }

    @Override
    public ClusterServiceFactory factory() {
        return null;
    }

    @Override
    public ClusterService read(PacketBuffer buffer) {
        return null;
    }
}
