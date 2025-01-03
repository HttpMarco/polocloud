package dev.httpmarco.polocloud.node.group.storage;

import com.google.gson.Gson;
import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.platform.SharedPlatform;
import dev.httpmarco.polocloud.common.gson.GsonPool;
import dev.httpmarco.polocloud.node.group.ClusterGroupImpl;
import dev.httpmarco.polocloud.node.group.ClusterGroupStorageFactory;
import dev.httpmarco.polocloud.node.platforms.serializer.SharedPlatformSerializer;
import dev.httpmarco.polocloud.node.utils.serializer.VersionSerializer;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class LocalFileStorageFactory implements ClusterGroupStorageFactory {

    private static final Gson GROUP_FACOTRY_GSON = GsonPool.newInstance(builder ->
            builder.registerTypeAdapter(SharedPlatform.class, new SharedPlatformSerializer())
                    .registerTypeAdapter(Version.class, new VersionSerializer())
    );
    private static final Path FACTORY_PATH = Path.of("local/groups");

    @SneakyThrows
    public LocalFileStorageFactory() {
        Files.createDirectories(FACTORY_PATH);
    }

    @Override
    @SneakyThrows
    public void store(ClusterGroup group) {
        Files.writeString(storagePath(group), GROUP_FACOTRY_GSON.toJson(group));
    }

    @Override
    @SneakyThrows
    public void destroy(ClusterGroup group) {
        Files.delete(storagePath(group));
    }

    @Override
    @SneakyThrows
    public List<ClusterGroup> searchAll() {
        return Arrays.stream(Objects.requireNonNull(FACTORY_PATH.toFile().listFiles())).filter(it -> it.getName().endsWith(".json")).map(it -> readGroup(it.toPath())).toList();
    }

    @Override
    public boolean alreadyDefined(@NotNull ClusterGroup group) {
        return Files.exists(storagePath(group));
    }

    private @NotNull Path storagePath(@NotNull ClusterGroup group) {
        return FACTORY_PATH.resolve(group.name() + ".json");
    }

    @SneakyThrows
    public ClusterGroup readGroup(Path path) {
        return GROUP_FACOTRY_GSON.fromJson(Files.readString(path), ClusterGroupImpl.class);
    }
}