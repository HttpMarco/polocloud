package dev.httpmarco.polocloud.base.groups;

import dev.httpmarco.osgan.files.Files;
import dev.httpmarco.osgan.utils.types.MessageUtils;
import dev.httpmarco.polocloud.api.groups.platforms.PlatformVersion;
import dev.httpmarco.polocloud.base.groups.platforms.BungeeCordPlatform;
import dev.httpmarco.polocloud.base.groups.platforms.PaperPlatform;
import dev.httpmarco.polocloud.base.groups.platforms.Platform;
import dev.httpmarco.polocloud.base.groups.platforms.VelocityPlatform;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import dev.httpmarco.polocloud.runner.RunnerBootstrap;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import java.nio.file.Path;
import java.util.*;

public class CloudGroupPlatformService {
    public static final String PROXY_SECRET = MessageUtils.randomString(8);
    private static final Path PLATFORM_FOLDER = Path.of("local/platforms");

    private final Set<Platform> platforms = new HashSet<>();

    public CloudGroupPlatformService() {

        Files.createDirectoryIfNotExists(PLATFORM_FOLDER);

        this.platforms.add(new PaperPlatform());
        this.platforms.add(new VelocityPlatform());
        this.platforms.add(new BungeeCordPlatform());
    }

    public boolean isValidPlatform(String platform) {
        return platforms.stream().anyMatch(it -> it.possibleVersions().stream().anyMatch(v -> v.version().equalsIgnoreCase(platform)));
    }

    public Platform find(String platform) {
        return platforms.stream().filter(it -> it.possibleVersions().stream().anyMatch(v -> v.version().equalsIgnoreCase(platform))).findFirst().orElse(null);
    }

    private boolean isPlatformAvailableForRuntime(String platform) {
        return java.nio.file.Files.exists(PLATFORM_FOLDER.resolve(platform).resolve("server.jar"));
    }

    @SneakyThrows
    public void preparePlatform(LocalCloudService cloudService) {
        String platformName = cloudService.group().platform().version();
        var platform = find(platformName);

        if (!isPlatformAvailableForRuntime(platformName)) {
            platform.download(platformName);
        }

        var cacheDirectory = PLATFORM_FOLDER.resolve(platformName).resolve("cache");
        if (java.nio.file.Files.exists(cacheDirectory)) {
            FileUtils.copyDirectory(cacheDirectory.toFile(), cloudService.runningFolder().toFile());
        }
        java.nio.file.Files.copy(PLATFORM_FOLDER.resolve(platformName).resolve("server.jar"), cloudService.runningFolder().resolve(platformName + ".jar"));

        platform.prepare(cloudService);

        // append server icon
        var serverIconPath = cloudService.runningFolder().resolve("server-icon.png");
        if (!java.nio.file.Files.exists(serverIconPath)) {
            java.nio.file.Files.copy(Objects.requireNonNull(RunnerBootstrap.LOADER.getResourceAsStream("server-files/server-icon.png")), serverIconPath);
        }
    }


    public List<PlatformVersion> validPlatformVersions() {
        var versions = new ArrayList<PlatformVersion>();
        for (var platform : this.platforms) {
            versions.addAll(platform.possibleVersions());
        }
        return versions;
    }
}