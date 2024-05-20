package dev.httpmarco.polocloud.base.groups;

import dev.httpmarco.osgan.files.Files;
import dev.httpmarco.polocloud.base.groups.platforms.PaperMCPlatform;
import dev.httpmarco.polocloud.base.groups.platforms.Platform;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class CloudGroupPlatformService {

    private static final Path PLATFORM_FOLDER = Path.of("local/platforms");
    private final Set<Platform> platforms = new HashSet<>();

    public CloudGroupPlatformService() {

        Files.createDirectoryIfNotExists(PLATFORM_FOLDER);

        this.platforms.add(new PaperMCPlatform("velocity"));
        this.platforms.add(new PaperMCPlatform("paper"));
    }


    public boolean isValidPlatform(String platform) {
        return platforms.stream().anyMatch(it -> it.possibleVersions().stream().anyMatch(v -> v.equalsIgnoreCase(platform)));
    }
}
