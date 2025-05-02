package dev.httpmarco.polocloud.suite.platforms.factory;

import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.platforms.Platform;
import dev.httpmarco.polocloud.suite.platforms.PlatformVersion;
import dev.httpmarco.polocloud.suite.services.ClusterLocalServiceImpl;
import dev.httpmarco.polocloud.suite.utils.PathUtils;
import dev.httpmarco.polocloud.suite.utils.downloading.Downloader;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Log4j2
public final class LocalPlatformFactory implements PlatformFactory {

    private static final Path PLATFORM_PATH = PathUtils.defineDirectory("local/platforms");

    @SneakyThrows
    @Override
    public void downloadPlatform(Platform platform, PlatformVersion version) {
        var platformPath = PLATFORM_PATH.resolve(platform.name())
                .resolve(version.version())
                .resolve(platform.name() + "-" + version.version() + "-" + version.buildId() + ".jar");

        if (Files.exists(platformPath)) {
            // already downloaded -> can start
            return;
        }

        platformPath.getParent().toFile().mkdirs();

        Downloader.of(platform.url()
                .replace("%version%", version.version())
                .replace("%buildId%", version.buildId()))
                .file(platformPath.toString());
    }

    @Override
    public void bindPlatform(ClusterService service) {

        if (service instanceof ClusterLocalServiceImpl localService) {

            var platform = PolocloudSuite.instance().platformProvider().findPlatform(localService.group().platform().name());
            var platformVersion = PolocloudSuite.instance().platformProvider().findPlatformVersion(localService.group().platform());

            if (platform == null || platformVersion == null) {
                log.error("cannot bind platform to {} with name {}", service.name(), platform.name());
                return;
            }

            this.downloadPlatform(platform, platformVersion);

            var platformFileName = platform.name() + "-" + platformVersion.version() + "-" + platformVersion.buildId() + ".jar";
            var platformPath = PLATFORM_PATH.resolve(platform.name())
                    .resolve(platformVersion.version())
                    .resolve(platformFileName);

            try {
                Files.copy(platformPath, localService.path().resolve(platformFileName));
            } catch (IOException e) {
                log.error("Failed to copy platform boot file to {}", platformPath, e);
            }
        } else {
            log.warn("Cannot prepare platform binding for {}.", service.name());
        }
    }
}
