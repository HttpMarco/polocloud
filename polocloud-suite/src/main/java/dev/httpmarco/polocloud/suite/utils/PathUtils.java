package dev.httpmarco.polocloud.suite.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Log4j2
@UtilityClass
public final class PathUtils {

    public @NotNull Path defineDirectory(String pathUrl) {
        var path = Path.of(pathUrl);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            log.info("Failed to create directories for dependencies.");
        }
        return path;
    }

    @SneakyThrows
    public void deleteDirectory(@NotNull File directoryToBeDeleted) {
        var allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
    }
}
