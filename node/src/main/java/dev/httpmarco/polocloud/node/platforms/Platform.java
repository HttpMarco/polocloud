package dev.httpmarco.polocloud.node.platforms;

import dev.httpmarco.polocloud.api.Detail;
import dev.httpmarco.polocloud.api.platforms.PlatformGroupDisplay;
import dev.httpmarco.polocloud.api.platforms.PlatformType;
import dev.httpmarco.polocloud.launcher.util.FileSystemUtils;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.platforms.file.PlatformFile;
import dev.httpmarco.polocloud.node.platforms.patcher.PlatformPatcher;
import dev.httpmarco.polocloud.node.platforms.versions.PlatformUrlVersion;
import dev.httpmarco.polocloud.node.services.ClusterLocalServiceImpl;
import dev.httpmarco.polocloud.node.util.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.*;
import java.util.List;

@Slf4j
@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class Platform implements Detail {

    private String id;
    private PlatformType type;

    private final String pluginDir;
    private final String pluginData;
    // the possible plugin dir, if not the main folder is enough
    private @Nullable String pluginDataPath;

    private final @Nullable List<String> arguments;
    private final List<PlatformPatcher> patchers;

    private final List<PlatformVersion> versions;
    private final List<PlatformFile> files;

    @SneakyThrows
    public void prepare(@NotNull PlatformGroupDisplay display, @NotNull ClusterLocalServiceImpl service) {
        var platform = service.group().platform();

        // download only if not exists
        this.download(display, service);

        //copy platform jar and maybe patch files
        DirectoryActions.copyDirectoryContents(Path.of("local/platforms/" + platform.platform() + "/" + platform.version()), service.runningDir());

        for (var file : files) {
            var strategy = file.strategy();
            var target = service.runningDir().resolve(file.file());
            var fileType = FileType.define(file.file());

            switch (strategy) {
                case COPY_FROM_CLASSPATH_IF_NOT_EXISTS -> {
                    if (!Files.exists(target)) {
                        FileSystemUtils.copyClassPathFile(this.getClass().getClassLoader(), "platforms/" + platform.platform() + "/" + file.file(), target.toString());
                    }
                }
                case DIRECT_CREATE -> {
                    Files.deleteIfExists(target);

                    target.getParent().toFile().mkdirs();
                    Files.createFile(target);
                }
                case APPEND_OR_REPLACE -> {
                    if (!Files.exists(target)) {
                        target.getParent().toFile().mkdirs();
                        Files.createFile(target);
                    }
                }
            }


            if (!file.replacements().isEmpty()) {
                var replacer = new ConfigManipulator(target.toFile());
                for (var replacement : file.replacements()) {
                    var content = replacement.value()
                            .replaceAll("%hostname%", service.hostname())
                            .replaceAll("%port%", String.valueOf(service.port()))
                            .replaceAll("%bungeecord_use%", String.valueOf(Node.instance().groupProvider().groups().stream().anyMatch(it -> it.platform().platform().equalsIgnoreCase("bungeecord"))))
                            .replaceAll("%velocity_use%", String.valueOf(Node.instance().groupProvider().groups().stream().anyMatch(it -> it.platform().platform().equalsIgnoreCase("velocity"))));
                    replacer.rewrite(s -> s.startsWith(replacement.indicator()), fileType.replacer().apply(new Pair<>(replacement.indicator(), content)));
                }
                replacer.write();
            }

            for (String append : file.appends()) {
                var content = append
                        .replaceAll("%forwarding_secret%", PlatformService.FORWARDING_SECRET)
                        .replaceAll("%velocity_use%", String.valueOf(Node.instance().groupProvider().groups().stream().anyMatch(it -> it.platform().platform().equalsIgnoreCase("velocity"))));
                Files.writeString(target, Files.readString(target) + content);
            }
        }
    }


    @SneakyThrows
    public void download(@NotNull PlatformGroupDisplay display, ClusterLocalServiceImpl localService) {
        var version = versions.stream().filter(it -> it.version().equalsIgnoreCase(display.version())).findFirst().orElseThrow();

        var platformDir = Path.of("local/platforms/" + display.platform() + "/" + display.version());

        if (!Files.exists(platformDir)) {
            platformDir.toFile().mkdirs();
        }

        var file = platformDir.resolve(display.details() + ".jar");


        if (!Files.exists(file)) {

            if(version instanceof PlatformUrlVersion urlVersion) {
                //copy bytes into the file
                Downloader.download(urlVersion.url(), file);
            }

            for (var patcher : patchers) {
                patcher.patch(file.toFile(), localService);
            }
        }
    }

    @Override
    public String details() {
        return versions.size() + " versions&8, &7type &8= &7" + type;
    }
}
