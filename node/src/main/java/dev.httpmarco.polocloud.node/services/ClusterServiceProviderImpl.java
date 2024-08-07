package dev.httpmarco.polocloud.node.services;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceFactory;
import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.packets.resources.services.ClusterSyncRegisterServicePacket;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@Log4j2
@Getter
@Accessors(fluent = true)
public final class ClusterServiceProviderImpl extends ClusterServiceProvider {

    private final List<ClusterService> services = new CopyOnWriteArrayList<>();
    private final ClusterServiceFactory factory = new ClusterServiceFactoryImpl();
    private final ClusterServiceQueue clusterServiceQueue = new ClusterServiceQueue();

    public ClusterServiceProviderImpl() {
        Node.instance().clusterProvider().localNode().transmit().listen(ClusterSyncRegisterServicePacket.class, (it, packet) -> {
            services.add(packet.service());
            log.info("The service &8'&f{}&8' &7is starting now&8...", packet.service().name());
        });
    }

    @Override
    public @NotNull CompletableFuture<List<ClusterService>> servicesAsync() {
        return CompletableFuture.completedFuture(services);
    }

    @Override
    public @NotNull CompletableFuture<ClusterService> findAsync(UUID id) {
        return CompletableFuture.completedFuture(services.stream().filter(it -> it.id().equals(id)).findFirst().orElse(null));
    }

    @Override
    public CompletableFuture<ClusterService> findAsync(String name) {
        return CompletableFuture.completedFuture(services.stream().filter(it -> it.name().equals(name)).findFirst().orElse(null));
    }

    @Contract(pure = true)
    @Override
    public @Nullable ClusterService read(PacketBuffer buffer) {
        return null;
    }
}
