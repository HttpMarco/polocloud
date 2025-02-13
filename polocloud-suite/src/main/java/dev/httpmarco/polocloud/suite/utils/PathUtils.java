package dev.httpmarco.polocloud.suite.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class PathUtils {

    private static final Logger log = LogManager.getLogger(PathUtils.class);

    public static Path defineDirectory(String pathUrl) {
        var path = Path.of(pathUrl);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            log.info("Failed to create directories for dependencies.");
        }
        return path;
    }
}
