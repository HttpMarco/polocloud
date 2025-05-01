package dev.httpmarco.polocloud.suite.platforms;

import dev.httpmarco.polocloud.api.platform.SharedPlatform;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.platforms.commands.PlatformCommand;
import dev.httpmarco.polocloud.suite.utils.downloading.Downloader;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Log4j2
@Accessors(fluent = true)
public class PlatformProvider {

    private static final String TABLE_OF_PLATFORMS = "https://raw.githubusercontent.com/HttpMarco/polocloud/refs/heads/master/polocloud-metadata/";
    private final List<Platform> platforms = new ArrayList<>();

    public PlatformProvider() {
        this.refreshPlatformData();

        PolocloudSuite.instance().commandService().registerCommand(new PlatformCommand(this));
        log.info("Loaded {} platforms", platforms.size());
    }

    public void refreshPlatformData() {
        this.platforms.clear();
        var table = Downloader.of(TABLE_OF_PLATFORMS + "metadata-table.json").gson(PlatformTable.class);

        for (var proxyId : table.availableProxies()) {
            platforms.add(Downloader.of(TABLE_OF_PLATFORMS + "proxy/" + proxyId + "/metadata.json").gson(Platform.class));
        }
        // todo load local platforms
    }

    public @Nullable Platform findPlatform(String name) {
        return platforms.stream().filter(platform -> platform.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Nullable
    public Platform findSharedInstance(SharedPlatform platform) {
       return this.platforms.stream().filter(it -> it.name().equalsIgnoreCase(platform.name())).findFirst().orElse(null);
    }
}