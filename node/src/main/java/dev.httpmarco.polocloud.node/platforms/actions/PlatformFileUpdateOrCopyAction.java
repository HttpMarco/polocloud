package dev.httpmarco.polocloud.node.platforms.actions;

import dev.httpmarco.polocloud.launcher.util.FileSystemUtils;
import dev.httpmarco.polocloud.node.services.ClusterLocalServiceImpl;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;

@Slf4j
@Getter
@Accessors(fluent = true)
public final class PlatformFileUpdateOrCopyAction extends AbstractPlatformAction {

    private final String fileName;
    private final String key;
    private final String value;

    public PlatformFileUpdateOrCopyAction(String fileName, String key, String value) {
        super("file-update-or-copy");
        this.fileName = fileName;
        this.key = key;
        this.value = value;
    }

    @Override
    @SneakyThrows
    public void run(@NotNull ClusterLocalServiceImpl service) {
        var file = service.runningDir().resolve(fileName).toFile();

        log.info("test");
        if (!file.exists()) {
            log.info("platforms/files/" + fileName);
            log.info(file.getAbsolutePath());
            FileSystemUtils.copyClassPathFile(this.getClass().getClassLoader(), "platforms/files/" + fileName, file.getAbsolutePath());
        }

        final var lines = Files.readAllLines(file.toPath());

        try (final var fileWriter = new FileWriter(file)) {
            for (var line : lines) {

                if (line.startsWith(key)) {
                    line = value.replaceAll("%port%", String.valueOf(service.port()))
                            .replaceAll("%hostname%", service.hostname());
                }

                fileWriter.write(line + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
