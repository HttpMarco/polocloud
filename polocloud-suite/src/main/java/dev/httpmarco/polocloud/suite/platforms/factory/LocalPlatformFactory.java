package dev.httpmarco.polocloud.suite.platforms.factory;

import dev.httpmarco.polocloud.api.platform.PlatformType;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.dependencies.pool.DependencyPool;
import dev.httpmarco.polocloud.suite.platforms.Platform;
import dev.httpmarco.polocloud.suite.platforms.PlatformVersion;
import dev.httpmarco.polocloud.suite.platforms.files.FilePrepareProcess;
import dev.httpmarco.polocloud.suite.services.ClusterLocalService;
import dev.httpmarco.polocloud.suite.services.ClusterLocalServiceImpl;
import dev.httpmarco.polocloud.suite.utils.PathUtils;
import dev.httpmarco.polocloud.suite.utils.downloading.Downloader;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

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

    @SneakyThrows
    @Override
    public void bindPlatform(ClusterService service) {

        if (service instanceof ClusterLocalServiceImpl localService) {

            var platform = PolocloudSuite.instance().platformProvider().findPlatform(localService.group().platform().name());
            var platformVersion = PolocloudSuite.instance().platformProvider().findPlatformVersion(localService.group().platform());

            if (platform == null || platformVersion == null) {
                log.error(PolocloudSuite.instance().translation().get("suite.platform.bind.failed", service.name(), platform.name()));
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
                log.error(PolocloudSuite.instance().translation().get("suite.platform.copy.failed", platformPath), e);
            }


            // edit the prepared process files
            for (FilePrepareProcess prepareProcess : platform.filePrepareProcess()) {
                var file = localService.path().resolve(prepareProcess.file().getName());

                switch (prepareProcess.flag()) {
                    case REPLACE_ALL -> {

                        var content = new StringBuilder();
                        for (var keys : prepareProcess.content().keySet()) {
                            var value = prepareProcess.content().get(keys);
                            if (value.isEmpty()) {
                                content.append(replacePlaceHolder(keys, localService)).append("\n");
                            } else {
                                content.append(keys).append("=").append(replacePlaceHolder(value, localService)).append("\n");
                            }
                        }

                        Files.write(file, content.toString().getBytes());
                    }
                    case CREATE_OR_UPDATE -> {
                        if (Files.exists(file)) {
                            //todo update here
                        } else {
                            var content = new StringBuilder();
                            for (var keys : prepareProcess.content().keySet()) {
                                var value = prepareProcess.content().get(keys);

                                content.append(keys).append("=").append(replacePlaceHolder(value, localService)).append("\n");
                            }
                            Files.writeString(file, replacePlaceHolder(content.toString(), localService));
                        }

                    }
                    case FILE_CLONE_OR_UPDATE -> {
                        if (Files.exists(file)) {
                            // todo update here
                        } else {
                            // todo improve here
                            Files.copy(Objects.requireNonNull(LocalPlatformFactory.class.getClassLoader().getResourceAsStream("platforms/" + platform.type().name().toLowerCase() + "/" + platform.name() + "/" + prepareProcess.file().getName())), file);

                            // update port properties etc.
                            var content = Files.readString(localService.path().resolve(prepareProcess.file().getName()));
                            Files.write(file, replacePlaceHolder(content, localService).getBytes());
                        }
                    }
                }
            }

            // copy the bridge if needed
            if (platform.type() == PlatformType.PROXY) {
                var bridgePath = Path.of("local/libs/polocloud-" + platform.language().name().toLowerCase() + "-bridge-2.0.0.jar");
                var bridgeTarget = localService.path().resolve(platform.bridgePath());

                if (Files.notExists(bridgeTarget)) {
                    Files.createDirectories(bridgeTarget);
                }

                Files.copy(bridgePath, bridgeTarget.resolve(bridgePath.getFileName()));
            }

        } else {
            log.warn(PolocloudSuite.instance().translation().get("suite.platform.binding.unsupported", service.name()));
        }
    }

    private String replacePlaceHolder(String content, ClusterLocalService service) {
        return content.replace("[%PORT%]", String.valueOf(service.port()))
                .replace("[%ONLINE_MODE%]", String.valueOf(PolocloudSuite.instance().groupProvider().findAll().stream().noneMatch(it -> it.platform().type() == PlatformType.PROXY)))
                .replace("[%PROXY_SECRET%]", "18293j21893j");
    }
}
