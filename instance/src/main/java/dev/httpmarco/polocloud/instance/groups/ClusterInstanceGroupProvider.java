package dev.httpmarco.polocloud.instance.groups;

import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.packet.resources.group.GroupCollectionPacket;
import dev.httpmarco.polocloud.api.packet.resources.group.GroupDeletePacket;
import dev.httpmarco.polocloud.api.packet.resources.group.GroupExistsResponsePacket;
import dev.httpmarco.polocloud.api.packet.resources.group.SingleGroupPacket;
import dev.httpmarco.polocloud.api.platforms.PlatformGroupDisplay;
import dev.httpmarco.polocloud.api.platforms.PlatformType;
import dev.httpmarco.polocloud.api.properties.PropertiesBuffer;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class ClusterInstanceGroupProvider extends ClusterGroupProvider {

    @Override
    public @NotNull CompletableFuture<Set<ClusterGroup>> groupsAsync() {
        var future = new CompletableFuture<Set<ClusterGroup>>();
        ClusterInstance.instance().client().requestAsync("groups-all", GroupCollectionPacket.class).whenComplete((it, t) -> future.complete(it.groups()));
        return future;
    }

    @Override
    public @NotNull CompletableFuture<Boolean> existsAsync(String group) {
        var future = new CompletableFuture<Boolean>();
        ClusterInstance.instance().client().requestAsync("group-exists", GroupExistsResponsePacket.class, new CommunicationProperty().set("name", group)).whenComplete((it, t) -> future.complete(it.value()));
        return future;
    }

    @Contract(pure = true)
    @Override
    public @NotNull CompletableFuture<Optional<String>> deleteAsync(String group) {
        var future = new CompletableFuture<Optional<String>>();
        ClusterInstance.instance().client().requestAsync("group-delete", GroupDeletePacket.class, new CommunicationProperty().set("name", group)).whenComplete((it, t) -> future.complete(Optional.of(it.content())));
        return future;
    }

    @Contract(pure = true)
    @Override
    public @Nullable CompletableFuture<Optional<String>> createAsync(String name, String[] nodes, PlatformGroupDisplay platform, int maxMemory, boolean staticService, int minOnline, int maxOnline) {
        // todo
        return null;
    }

    @Contract(pure = true)
    @Override
    public @NotNull CompletableFuture<ClusterGroup> findAsync(@NotNull String group) {
        var future = new CompletableFuture<ClusterGroup>();
        ClusterInstance.instance().client().requestAsync("group-finding", SingleGroupPacket.class, new CommunicationProperty().set("name", group)).whenComplete((it, t) -> future.complete(it.group()));
        return future;
    }

    @Contract("_ -> new")
    @Override
    public @NotNull ClusterGroup read(@NotNull PacketBuffer buffer) {
        var name = buffer.readString();
        var maxMemory = buffer.readInt();
        var maxPlayers = buffer.readInt();
        var minOnlineServerInstances = buffer.readInt();
        var maxOnlineServerInstances = buffer.readInt();
        var staticService = buffer.readBoolean();
        var platform = new PlatformGroupDisplay(buffer.readString(), buffer.readString(), buffer.readEnum(PlatformType.class));
        var amountOfNodes = buffer.readInt();
        var nodes = new String[amountOfNodes];

        for (int i = 0; i < amountOfNodes; i++) {
            nodes[i] = buffer.readString();
        }

        var amountOfTemplates = buffer.readInt();
        var templates = new String[amountOfTemplates];

        for (int i = 0; i < amountOfTemplates; i++) {
            templates[i] = buffer.readString();
        }

        var propertiesPool = new PropertiesPool();
        PropertiesBuffer.read(buffer, propertiesPool);

        return new ClusterInstanceGroupImpl(name, platform, templates, nodes, maxMemory, maxPlayers, staticService, minOnlineServerInstances, maxOnlineServerInstances, propertiesPool);
    }
}
