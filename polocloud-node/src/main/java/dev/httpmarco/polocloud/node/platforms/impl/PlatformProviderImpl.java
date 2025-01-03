package dev.httpmarco.polocloud.node.platforms.impl;

import com.google.gson.Gson;
import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.api.platform.SharedPlatform;
import dev.httpmarco.polocloud.common.downloading.Downloader;
import dev.httpmarco.polocloud.common.gson.GsonPool;
import dev.httpmarco.polocloud.node.platforms.Platform;
import dev.httpmarco.polocloud.node.platforms.PlatformMap;
import dev.httpmarco.polocloud.node.platforms.PlatformProvider;
import dev.httpmarco.polocloud.node.platforms.PlatformVersion;
import dev.httpmarco.polocloud.node.platforms.serializer.PlatformSerializer;
import dev.httpmarco.polocloud.node.utils.serializer.VersionSerializer;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Getter
@Log4j2
@Accessors(fluent = true)
public final class PlatformProviderImpl implements PlatformProvider {

    private static final Gson PLATOFRM_GSON = GsonPool.newInstance(gsonBuilder -> gsonBuilder.registerTypeAdapter(Platform.class, new PlatformSerializer()).registerTypeAdapter(Version.class, new VersionSerializer()));
    private static final String PLATFORM_MAP_URL = "https://raw.githubusercontent.com/HttpMarco/polocloud/refs/heads/master/platforms.json";
    private static final Path PLATFORM_MAP_FILE = Path.of("local/platforms.json");

    private final PlatformMap currentMap;

    @SneakyThrows
    public PlatformProviderImpl() {

        Files.createDirectories(PLATFORM_MAP_FILE.getParent());

        if (!Files.exists(PLATFORM_MAP_FILE)) {
            Files.writeString(PLATFORM_MAP_FILE, PLATOFRM_GSON.toJson(this.originalPlatformMap()));
            log.info("Downloading original platform map...");
        } else {
            var originalMap = this.originalPlatformMap();
            // todo check version and warn
        }

        this.currentMap = this.loadLocalPlatformMap();
        log.info("Loaded {} platforms, with {} versions", currentMap.platforms().length, Arrays.stream(currentMap.platforms()).mapToInt(value -> value.versions().size()).sum());
    }

    public PlatformMap originalPlatformMap() {
        return renderGsonPlatformMap(Downloader.of(PLATFORM_MAP_URL).plain());
    }

    @SneakyThrows
    public PlatformMap loadLocalPlatformMap() {
        return this.renderGsonPlatformMap(Files.readString(PLATFORM_MAP_FILE));
    }

    private PlatformMap renderGsonPlatformMap(String gson) {
        return PLATOFRM_GSON.fromJson(gson, PlatformMap.class);
    }

    @Override
    public Platform indexingShared(SharedPlatform sharedPlatform) {
        return Arrays.stream(this.currentMap.platforms())
                .filter(it -> it.name().equals(sharedPlatform.name()) && it.versions()
                        .stream()
                        .anyMatch(v -> v.version().equals(sharedPlatform.version())))
                .findFirst()
                .orElse(null);
    }

    @Override
    public @Nullable PlatformVersion searchPlatformVersion(String platformName, String version) {
        for (var platform : currentMap.platforms()) {
            if (!platform.name().equals(platformName)) {
                continue;
            }

            return platform.versions().stream()
                    .filter(it -> it.version().originalVersion().equals(version))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}