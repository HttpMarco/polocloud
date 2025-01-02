package dev.httpmarco.polocloud.node.group.storage;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.common.gson.GsonPool;
import dev.httpmarco.polocloud.node.group.ClusterGroupStorageFactory;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;

public final class LocalFileStorageFactory implements ClusterGroupStorageFactory {

    private static final Path FACTORY_PATH = Path.of("local/groups");

    @SneakyThrows
    public LocalFileStorageFactory() {
        Files.createDirectories(FACTORY_PATH);
    }

    @Override
    @SneakyThrows
    public void store(ClusterGroup group) {
        Files.writeString(storagePath(group), GsonPool.PRETTY_GSON.toJson(group));
    }

    @Override
    @SneakyThrows
    public void destroy(ClusterGroup group) {
        Files.delete(storagePath(group));
    }

    @Override
    public void update(ClusterGroup group) {
        this.store(group);
    }

    @Override
    public boolean alreadyDefined(@NotNull ClusterGroup group) {
        return Files.exists(storagePath(group));
    }

    private @NotNull Path storagePath(@NotNull ClusterGroup group) {
        return FACTORY_PATH.resolve(group.name() + ".json");
    }
}