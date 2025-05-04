package dev.httpmarco.polocloud.suite.groups.storage;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.storage.ClusterStorage;
import dev.httpmarco.polocloud.suite.groups.ClusterGroupImpl;
import dev.httpmarco.polocloud.suite.utils.GsonInstance;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class is only for local use. The Groups are safed in the local file system.
 */
@Getter
@Log4j2
@Accessors(fluent = true)
public final class LocalClusterGroupStorage implements ClusterStorage<String, ClusterGroup> {

    private static final Path GROUPS_PATH = Paths.get("local/groups");
    // all cached groups
    private List<ClusterGroup> groups;

    private @Nullable ClusterGroup convertFromJson(@NotNull File file) {
        try {
            return GsonInstance.DEFAULT.fromJson(Files.readString(file.toPath()), ClusterGroupImpl.class);
        } catch (IOException e) {
            log.error(PolocloudSuite.instance().translation().get("suite.group.error.load", file.getName()), e);
        }
        return null;
    }

    private String convertToJson(ClusterGroup group) {
        return GsonInstance.DEFAULT.toJson(group);
    }

    @Override
    public void initialize() {
        if (!Files.exists(GROUPS_PATH)) {
            try {
                Files.createDirectories(GROUPS_PATH);
            } catch (IOException e) {
                log.error(PolocloudSuite.instance().translation().get("suite.group.error.createDir", GROUPS_PATH), e);
            }
        }

        this.groups = new ArrayList<>(Arrays.stream(Objects.requireNonNull(GROUPS_PATH.toFile().listFiles())).map(this::convertFromJson).filter(Objects::nonNull).toList());
    }

    @Contract(pure = true)
    @Override
    public @NotNull @Unmodifiable List<ClusterGroup> items() {
        return this.groups;
    }

    @Override
    public void publish(@NotNull ClusterGroup group) {
        try {
            Files.writeString(GROUPS_PATH.toAbsolutePath().resolve(group.name() + ".json"), convertToJson(group));
        } catch (IOException e) {
            log.error(PolocloudSuite.instance().translation().get("suite.group.error.save", group.name()), e);
        }
        this.groups.add(group);
    }

    @Override
    public ClusterGroup singleton(String identifier) {
        return this.groups.stream().filter(it -> it.name().equals(identifier)).findFirst().orElse(null);
    }

    @Override
    public void destroy(String identifier) {
        this.groups.remove(singleton(identifier));

        try {
            Files.delete(GROUPS_PATH.toAbsolutePath().resolve(identifier + ".json"));
        } catch (IOException e) {
            log.error(PolocloudSuite.instance().translation().get("suite.group.error.delete", identifier), e);
        }
    }

    @Override
    public String extreactIdentifier(ClusterGroup item) {
        return "";
    }
}
