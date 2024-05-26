package dev.httpmarco.polocloud.base.groups;

import dev.httpmarco.osgan.files.Files;
import dev.httpmarco.osgan.utils.types.MessageUtils;
import dev.httpmarco.polocloud.base.groups.platforms.PaperPlatform;
import dev.httpmarco.polocloud.base.groups.platforms.Platform;
import dev.httpmarco.polocloud.base.groups.platforms.VelocityPlatform;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import dev.httpmarco.polocloud.runner.RunnerBootstrap;
import lombok.SneakyThrows;

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
    }


    public boolean isValidPlatform(String platform) {
        return platforms.stream().anyMatch(it -> it.possibleVersions().stream().anyMatch(v -> v.equalsIgnoreCase(platform)));
    }

    public Platform find(String platform) {
        return platforms.stream().filter(it -> it.possibleVersions().stream().anyMatch(v -> v.equalsIgnoreCase(platform))).findFirst().orElse(null);
    }

    private boolean isPlatformAvailableForRuntime(String platform) {
        return java.nio.file.Files.exists(PLATFORM_FOLDER.resolve(platform + ".jar"));
    }

    @SneakyThrows
    public void preparePlatform(LocalCloudService cloudService) {
        String platformName = cloudService.group().platform();
        var platform = find(platformName);

        if (!isPlatformAvailableForRuntime(platformName)) {
            platform.download(platformName);
        }

        var platformFile = platformName + ".jar";
        java.nio.file.Files.copy(PLATFORM_FOLDER.resolve(platformName + ".jar"), cloudService.runningFolder().resolve(platformFile));
        platform.prepare(cloudService);

        // append server icon
        var serverIconPath = cloudService.runningFolder().resolve("server-icon.png");
        if (!java.nio.file.Files.exists(serverIconPath)) {
            java.nio.file.Files.copy(Objects.requireNonNull(RunnerBootstrap.LOADER.getResourceAsStream("server-files/server-icon.png")), serverIconPath);
        }
    }


    public List<String> validPlatformVersions() {
        var versions = new ArrayList<String>();
        for (var platform : this.platforms) {
            versions.addAll(platform.possibleVersions());
        }
        return versions;
    }
}