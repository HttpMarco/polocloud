package dev.httpmarco.polocloud.suite.platforms;

import dev.httpmarco.polocloud.suite.utils.downloading.Downloader;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class PlatformProvider {

    private static final String TABLE_OF_PLATFORMS = "https://raw.githubusercontent.com/HttpMarco/polocloud/refs/heads/master/polocloud-metadata/";
    private final List<Platform> platforms = new ArrayList<>();

    public PlatformProvider() {
        this.refreshPlatformData();

        log.info("Loaded " + platforms.size() + " platforms");
    }

    public void refreshPlatformData() {
        this.platforms.clear();
        var table = Downloader.of(TABLE_OF_PLATFORMS + "metadata-table.json").gson(PlatformTable.class);

        for (var proxyId : table.availableProxies()) {
            platforms.add(Downloader.of(TABLE_OF_PLATFORMS + "proxy/" + proxyId + "/metadata.json").gson(Platform.class));
        }
        // todo load local platforms
    }
}