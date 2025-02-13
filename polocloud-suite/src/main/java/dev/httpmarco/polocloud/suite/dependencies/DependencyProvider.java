package dev.httpmarco.polocloud.suite.dependencies;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DependencyProvider {

    private static final Logger log = LogManager.getLogger(DependencyProvider.class);
    private static final Path DEPENDENCY_DIRECTORY = Paths.get("local/dependencies");

    static {
        try {
            Files.createDirectories(DEPENDENCY_DIRECTORY);
        } catch (IOException e) {
            log.info("Failed to create directories for dependencies.");
        }
    }

}
