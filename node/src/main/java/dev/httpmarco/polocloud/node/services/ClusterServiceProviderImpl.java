package dev.httpmarco.polocloud.node.services;

import dev.httpmarco.osgan.networking.CommunicationFuture;
import dev.httpmarco.osgan.networking.channel.ChannelTransmit;
import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.Closeable;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.event.EventPoolRegister;
import dev.httpmarco.polocloud.api.event.impl.services.ServiceOnlineEvent;
import dev.httpmarco.polocloud.api.packet.IntPacket;
import dev.httpmarco.polocloud.api.packet.RedirectPacket;
import dev.httpmarco.polocloud.api.packet.resources.event.EventCallPacket;
import dev.httpmarco.polocloud.api.packet.resources.player.PlayerCollectionPacket;
import dev.httpmarco.polocloud.api.packet.resources.services.*;
import dev.httpmarco.polocloud.api.platforms.PlatformType;
import dev.httpmarco.polocloud.api.properties.PropertiesBuffer;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.api.services.*;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.packets.resources.services.ClusterSyncRegisterServicePacket;
import dev.httpmarco.polocloud.node.packets.resources.services.ClusterSyncUnregisterServicePacket;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.*;

@Log4j2
@Getter
@Accessors(fluent = true)
public final class ClusterServiceProviderImpl extends ClusterServiceProvider implements Closeable {

    @Setter
    private String serviceProxyToken;
    private final List<ClusterService> services = new CopyOnWriteArrayList<>();
    private final ClusterServiceFactory factory = new ClusterServiceFactoryImpl();
    private final ClusterServiceQueue clusterServiceQueue = new ClusterServiceQueue();
    private final ClusterLocalServiceReadingThread logReadingThread = new ClusterLocalServiceReadingThread();

    public ClusterServiceProviderImpl() {
        var localNode = Node.instance().clusterProvider().localNode();

        localNode.transmit().listen(ClusterSyncRegisterServicePacket.class, (it, packet) -> {
            services.add(packet.service());
            log.info("The service &8'&f{}&8' &7is starting now&8...", packet.service().name());
        });

        localNode.transmit().responder("service-find", property -> new ClusterServicePacket(property.has("id") ? find(property.getUUID("id")) : find(property.getString("name"))));

        localNode.transmit().listen(ServiceShutdownCallPacket.class, (transmit, packet) -> {
            var service = Node.instance().serviceProvider().find(packet.id());

            if (service == null) {
                log.error("Tried to shut down a service, but the service is not present in the cluster. {}", packet.id());
                return;
            }

            service.shutdown();
        });

        localNode.transmit().listen(ServiceOnlinePacket.class, (transmit, packet) -> {
            var service = find(packet.id());
            if (service == null) {
                transmit.channel().close();
                return;
            }

            if (service instanceof ClusterLocalServiceImpl localService) {
                localService.state(ClusterServiceState.ONLINE);
                localService.update();
            } else {
                transmit.channel().close();
                return;
            }

            log.info("The service &8'&f{}&8' &7is online&8.", service.name());
            Node.instance().eventProvider().factory().call(new ServiceOnlineEvent(service));
        });

        localNode.transmit().listen(ServiceCommandPacket.class, (transmit, packet) -> {
            var service = find(packet.id());

            if (isServiceChannel(transmit)) {
                // we know that we get the request from a node
                // now we can check in the local storage
                if (service instanceof ClusterLocalServiceImpl localService) {
                    localService.executeCommand(packet.command());
                } else {
                    log.error("A node send a command data for {} , but not a local instance.", packet.id());
                }
                return;
            }
            Node.instance().clusterProvider().find(service.runningNode()).transmit().sendPacket(packet);
        });

        localNode.transmit().responder("service-log", property -> {
            var id = property.getUUID("id");
            var service = Node.instance().serviceProvider().find(id);

            if (service instanceof ClusterLocalServiceImpl localService) {
                return new ServiceLogPacket(localService.logs());
            }

            var logs = Node.instance().clusterProvider().find(service.runningNode()).transmit().request("service-log", ServiceLogPacket.class, property).logs();
            return new ServiceLogPacket(logs);
        });

        localNode.transmit().responder("service-players-count", property -> {
            var service = find(property.getUUID("id"));

            if (service instanceof ClusterLocalServiceImpl) {
                return new IntPacket(service.onlinePlayersCount());
            }

            return new IntPacket(Node.instance().clusterProvider().find(service.runningNode()).transmit().request("service-players-count",  IntPacket.class, property).value());
        });


        localNode.transmit().listen(RedirectPacket.class, (transmit, redirectPacket) -> {
            var target = redirectPacket.target();
            var service = find(target);

            if (service instanceof ClusterLocalServiceImpl localService) {
                localService.transmit().sendPacket(redirectPacket);
                return;
            }

            Node.instance().clusterProvider().find(service.runningNode()).transmit().sendPacket(redirectPacket);
        });

        localNode.transmit().responder("service-players", property -> new PlayerCollectionPacket(find(property.getUUID("id")).onlinePlayers()));
        localNode.transmit().responder("service-all", property -> new ServiceCollectionPacket(services));
        localNode.transmit().responder("service-filtering", property -> new ServiceCollectionPacket(find(property.getEnum("filter", ClusterServiceFilter.class))));

        localNode.transmit().listen(ClusterSyncUnregisterServicePacket.class, (transmit, packet) -> Node.instance().serviceProvider().services().removeIf(service -> service.id().equals(packet.id())));
        logReadingThread.start();
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
    public @NotNull CompletableFuture<ClusterService> findAsync(String name) {
        return CompletableFuture.completedFuture(services.stream().filter(it -> it.name().equals(name)).findFirst().orElse(null));
    }

    @Override
    public @NotNull CompletableFuture<List<ClusterService>> findAsync(@NotNull ClusterServiceFilter filter) {
        return CompletableFuture.completedFuture((switch (filter) {
            case ONLINE_SERVICES -> services.stream().filter(service -> service.state() == ClusterServiceState.ONLINE);
            case EMPTY_SERVICES -> services.stream().filter(ClusterService::isEmpty);
            case PLAYERS_PRESENT_SERVERS -> services.stream().filter(service -> !service.isEmpty());
            case SAME_NODE_SERVICES ->
                    services.stream().filter(it -> Node.instance().clusterProvider().localNode().data().name().equals(it.runningNode()));
            case FALLBACKS -> services.stream().filter(service -> service.group().fallback());
            case PROXIES -> services.stream().filter(it -> it.group().platform().type() == PlatformType.PROXY);
            case SERVERS -> services.stream().filter(it -> it.group().platform().type() == PlatformType.SERVER);
            case SERVICES -> services.stream().filter(it -> it.group().platform().type() == PlatformType.SERVICE);
            case LOWEST_FALLBACK ->
                    services.stream().filter(service -> service.group().fallback()).min(Comparator.comparingInt(ClusterService::onlinePlayersCount)).stream();
        }).toList());
    }

    @Contract(pure = true)
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

        return new ClusterServiceImpl(group, orderedId, id, port, hostname, runningNode, propertiesPool, maxPlayers, state);
    }

    public boolean isServiceChannel(ChannelTransmit transmit) {
        return this.services.stream().anyMatch(it -> it instanceof ClusterLocalServiceImpl localService && localService.transmit() != null && localService.transmit().equals(transmit));
    }

    public ClusterService find(ChannelTransmit transmit) {
        return this.services.stream().filter(it -> it instanceof ClusterLocalServiceImpl localService && localService.transmit() != null && localService.transmit().equals(transmit)).findFirst().orElse(null);
    }

    @Override
    public void close() {
        this.logReadingThread.interrupt();
    }
}
