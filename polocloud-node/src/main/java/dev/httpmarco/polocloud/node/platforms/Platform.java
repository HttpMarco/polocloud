package dev.httpmarco.polocloud.node.platforms;

import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.platform.PlatformType;
import dev.httpmarco.polocloud.common.downloading.Downloader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public final class Platform implements Named {

    private final String name;
    private final String icon;
    private final PlatformType type;
    private final String url;
    private final List<PlatformVersion> versions = new ArrayList<>();

    private void prepare(@NotNull PlatformVersion version) {
        var path = Path.of("platforms", name, version.version().originalVersion());

        if(Files.exists(path)) {
            return;
        }

        log.info("Downloading platform {} version {}", name, version.version());
        Downloader.of(url).file(path);
    }
}
