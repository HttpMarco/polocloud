package dev.httpmarco.polocloud.node.platforms.impl;

import dev.httpmarco.polocloud.common.gson.GsonPool;
import dev.httpmarco.polocloud.node.platforms.PlatformMap;
import dev.httpmarco.polocloud.node.platforms.PlatformProvider;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@Accessors(fluent = true)
public final class PlatformProviderImpl implements PlatformProvider {

    private static final String PLATFORM_MAP_URL = "https://polocloud.de/api/platforms";
    private static final Path PLATFORM_MAP_FILE = Path.of("local/platforms.json");

    private final PlatformMap currentMap;

    public PlatformProviderImpl() {
        this.currentMap = Files.exists(PLATFORM_MAP_FILE) ? this.loadLocalPlatformMap() : this.originalPlatformMap();
    }

    public PlatformMap originalPlatformMap() {
        return null;
    }

    @SneakyThrows
    public PlatformMap loadLocalPlatformMap() {
        return this.renderGsonPlatformMap(Files.readString(PLATFORM_MAP_FILE));
    }

    private PlatformMap renderGsonPlatformMap(String gson) {
        return GsonPool.PRETTY_GSON.fromJson(gson, PlatformMap.class);
    }
}