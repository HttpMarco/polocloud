package dev.httpmarco.polocloud.node.platforms.tasks;

import dev.httpmarco.polocloud.node.platforms.Platform;
import dev.httpmarco.polocloud.node.platforms.PlatformVersion;
import dev.httpmarco.polocloud.node.util.ChecksumCalculator;
import dev.httpmarco.polocloud.node.util.Downloader;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@Log4j2
@UtilityClass
public final class PlatformDownloadTask {

    private static final Path PLATFORM_DIR = Path.of("local/platforms");

    @SneakyThrows
    public CompletableFuture<Void> download(@NotNull Platform platform, @NotNull PlatformVersion version) {
        var versionPath = PLATFORM_DIR.resolve(platform.platform()).resolve(version.version());

        versionPath.toFile().mkdirs();

        var file = versionPath.resolve(platform.platform() + "-" + version.version() + ".jar");

        if (!Files.exists(file)) {
            Downloader.download(version.downloadLink(), file);
        } else if (version.checksum().equals(ChecksumCalculator.checksum(file))) {
            // all fine we can start
            return CompletableFuture.completedFuture(null);
        }

        if (!version.checksum().equals(ChecksumCalculator.checksum(file))) {
            // todo delete and redownload
        }

        if (platform.platformPatcher() == null) {
            return CompletableFuture.completedFuture(null);
        }

        return platform.platformPatcher().patch(file.toFile());
    }
}