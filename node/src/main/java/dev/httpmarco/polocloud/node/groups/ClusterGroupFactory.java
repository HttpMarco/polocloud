package dev.httpmarco.polocloud.node.groups;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.packet.resources.group.GroupCreatePacket;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.util.JsonUtils;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Log4j2
@UtilityClass
public final class ClusterGroupFactory {

    private static final Path GROUP_DIR = Path.of("local/groups");

    @SneakyThrows
    public void createLocalStorageGroup(@NotNull GroupCreatePacket packet, @NotNull ClusterGroupProvider clusterGroupProvider) {
        var group = new ClusterGroupImpl(
                packet.name(),
                packet.platformGroupDisplay(),
                packet.templates(),
                packet.nodes(),
                packet.maxMemory(),
                packet.staticService(),
                packet.minOnline(),
                packet.maxOnline()
        );

        if (packet.fallbackGroup()) {
            group = new ClusterGroupFallbackImpl(group);
        }

        // check every creation, if directory exists
        GROUP_DIR.toFile().mkdirs();
        var groupFile = GROUP_DIR.resolve(group.name() + ".json");
        Files.writeString(groupFile, JsonUtils.GSON.toJson(group));

        Node.instance().templatesProvider().prepareTemplate(group.templates());

        clusterGroupProvider.groups().add(group);
    }

    @SneakyThrows
    public void deleteLocalStorageGroup(String name, @NotNull ClusterGroupProvider clusterGroupProvider) {
        var clusterGroup = clusterGroupProvider.find(name);

        if (clusterGroup == null) {
            return;
        }

        var groupFile = GROUP_DIR.resolve(name + ".json");
        Files.deleteIfExists(groupFile);

        clusterGroup.services().forEach(ClusterService::shutdown);
        clusterGroupProvider.groups().removeIf(group -> group.name().equalsIgnoreCase(name));
    }

    @SneakyThrows
    public @NotNull Set<ClusterGroup> readGroups() {
        var groups = new HashSet<ClusterGroup>();

        if (!Files.exists(GROUP_DIR)) {
            return groups;
        }

        for (File file : Objects.requireNonNull(GROUP_DIR.toFile().listFiles())) {
            groups.add(JsonUtils.GSON.fromJson(Files.readString(file.toPath()), ClusterGroup.class));
        }
        return groups;
    }

}
