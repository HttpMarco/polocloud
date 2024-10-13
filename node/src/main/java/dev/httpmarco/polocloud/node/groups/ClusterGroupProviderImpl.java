package dev.httpmarco.polocloud.node.groups;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.Reloadable;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.packet.resources.group.*;
import dev.httpmarco.polocloud.api.platforms.PlatformGroupDisplay;
import dev.httpmarco.polocloud.api.platforms.PlatformType;
import dev.httpmarco.polocloud.api.properties.PropertiesBuffer;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.cluster.ClusterProvider;
import dev.httpmarco.polocloud.node.groups.requests.GroupCreationRequest;
import dev.httpmarco.polocloud.node.groups.requests.GroupDeletionRequest;
import dev.httpmarco.polocloud.node.groups.responder.GroupCreationResponder;
import dev.httpmarco.polocloud.node.groups.responder.GroupDeletionResponder;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Getter
@Accessors(fluent = true)
public final class ClusterGroupProviderImpl extends ClusterGroupProvider implements Reloadable {

    private final Set<ClusterGroup> groups;
    private final ClusterProvider clusterProvider;

    public ClusterGroupProviderImpl(@NotNull ClusterProvider clusterProvider) {
        this.clusterProvider = clusterProvider;

        var server = clusterProvider.localNode().server();
        server.listen(GroupCreatePacket.class, (transmit, packet) -> ClusterGroupFactory.createLocalStorageGroup(packet, this));
        server.listen(GroupDeletePacket.class, (transmit, packet) -> ClusterGroupFactory.deleteLocalStorageGroup(packet.name(), this));

        server.registerResponder("group-delete", property -> {
            try {
                return new GroupDeletePacket(GroupDeletionRequest.request(clusterProvider, property.getString("name")).get().get()); //TODO returns maybe null
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        server.registerResponder("group-finding", property -> new SingleGroupPacket(find(property.getString("name"))));
        server.registerResponder("group-exists", property -> new GroupExistsResponsePacket(exists(property.getString("name"))));
        server.registerResponder("groups-all", property -> new GroupCollectionPacket(groups()));
        server.registerResponder(GroupCreationRequest.TAG, property -> GroupCreationResponder.handle(this, clusterProvider, property));
        server.registerResponder(GroupDeletionRequest.TAG, property -> GroupDeletionResponder.handle(this, clusterProvider, property));

        server.listen(GroupRequestUpdatePacket.class, (transmit, groupRequestUpdatePacket) -> {
            if (Node.instance().clusterProvider().localHead()) {
                Node.instance().clusterProvider().broadcastAll(new GroupUpdatePacket(groupRequestUpdatePacket.group()));
                return;
            }
            Node.instance().clusterProvider().headNode().transmit().sendPacket(groupRequestUpdatePacket);
        });

        server.listen(GroupUpdatePacket.class, (transmit, groupUpdatePacket) -> {
            // change only the right things
            var current = find(groupUpdatePacket.group().name());

            // update all properties
            current.properties().pool().clear();
            current.properties().pool().putAll(groupUpdatePacket.group().properties().pool());

            // update all configs
            ClusterGroupFactory.updateLocalStorageGroup(current);
        });

        this.groups = ClusterGroupFactory.readGroups();

        if (!groups.isEmpty()) {
            log.info("Loading following groups: {}", String.join(", ", groups.stream().map(Named::name).toList()));
        }
    }

    @Override
    public @NotNull CompletableFuture<Set<ClusterGroup>> groupsAsync() {
        return CompletableFuture.completedFuture(groups);
    }

    @Override
    public @NotNull CompletableFuture<Boolean> existsAsync(String group) {
        return CompletableFuture.completedFuture(this.groups.stream().anyMatch(it -> it.name().equalsIgnoreCase(group)));
    }

    @Contract(pure = true)
    @Override
    public @Nullable CompletableFuture<Optional<String>> deleteAsync(String group) {
        return GroupDeletionRequest.request(clusterProvider, group);
    }

    @Override
    public CompletableFuture<Optional<String>> createAsync(String name, String[] nodes, PlatformGroupDisplay platform, int maxMemory, boolean staticService, int minOnline, int maxOnline) {
        return GroupCreationRequest.request(clusterProvider, name, nodes, platform, maxMemory, staticService, minOnline, maxOnline);
    }

    @Override
    public @NotNull CompletableFuture<ClusterGroup> findAsync(@NotNull String group) {
        return CompletableFuture.completedFuture(this.groups.stream().filter(it -> it.name().equalsIgnoreCase(group)).findFirst().orElse(null));
    }

    @Override
    public void reload() {

        this.groups.clear();
        if (Node.instance().clusterProvider().localHead()) {
            this.groups.addAll(ClusterGroupFactory.readGroups());
        } else {
            Node.instance().clusterProvider().headNode().requestAsync("groups-all", GroupCollectionPacket.class).whenComplete((it, t) -> {
                this.groups.addAll(it.groups());
                log.info("Successfully reload all group data.");
            });
            return;
        }

        log.info("Successfully reload all group data.");
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

        return new ClusterGroupImpl(name, platform, templates, nodes, maxMemory, maxPlayers, staticService, minOnlineServerInstances, maxOnlineServerInstances, propertiesPool);
    }
}
