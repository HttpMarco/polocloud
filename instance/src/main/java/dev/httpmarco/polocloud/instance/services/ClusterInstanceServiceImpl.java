package dev.httpmarco.polocloud.instance.services;

import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.packet.IntPacket;
import dev.httpmarco.polocloud.api.packet.RedirectPacket;
import dev.httpmarco.polocloud.api.packet.resources.player.PlayerCollectionPacket;
import dev.httpmarco.polocloud.api.packet.resources.services.ServiceCommandPacket;
import dev.httpmarco.polocloud.api.packet.resources.services.ServiceLogPacket;
import dev.httpmarco.polocloud.api.packet.resources.services.ServiceShutdownCallPacket;
import dev.httpmarco.polocloud.api.players.ClusterPlayer;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceState;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public final class ClusterInstanceServiceImpl implements ClusterService {

    private final UUID id;
    private final int orderedId;
    private final int port;
    private final String hostname;
    private final String runningNode;
    private final ClusterServiceState state;
    private final ClusterGroup group;
    private final PropertiesPool properties;

    private int maxPlayers;

    @Override
    public void shutdown() {
        ClusterInstance.instance().client().sendPacket(new ServiceShutdownCallPacket(this.id));
    }

    @Override
    public void executeCommand(String command) {
        ClusterInstance.instance().client().sendPacket(new ServiceCommandPacket(id, command));
    }

    @Override
    @SneakyThrows
    public List<String> logs() {
        var future = new CompletableFuture<List<String>>();
        ClusterInstance.instance().client().requestAsync("service-log", ServiceLogPacket.class, new CommunicationProperty().set("id", id)).whenComplete((it, t) -> future.complete(it.logs()));
        return future.get(5, TimeUnit.SECONDS);
    }

    @Override
    public void update() {
        //todo
    }

    @Override
    public @NotNull CompletableFuture<Integer> onlinePlayersCountAsync() {
        var future = new CompletableFuture<Integer>();
        ClusterInstance.instance().client().requestAsync("service-players-count", IntPacket.class, new CommunicationProperty().set("id", id)).whenComplete((it, t) -> future.complete(it.value()));
        return future;
    }

    @Override
    public @NotNull CompletableFuture<List<ClusterPlayer>> onlinePlayersAsync() {
        var future = new CompletableFuture<List<ClusterPlayer>>();
        ClusterInstance.instance().client().requestAsync("service-players", PlayerCollectionPacket.class, new CommunicationProperty().set("id", id)).whenComplete((it, t) -> future.complete(it.players()));
        return future;
    }

    @Override
    public String details() {
        return this.toString();
    }

    @Override
    public void sendPacket(Packet packet) {
        if (id.equals(ClusterInstance.instance().selfServiceId())) {
            // here we are allow to call the packet directly because we are on the same instance
            ClusterInstance.instance().client().call(packet, null);
            return;
        }
        ClusterInstance.instance().client().sendPacket(new RedirectPacket(id, packet));
    }
}
