package dev.httpmarco.polocloud.base.logging;

import dev.httpmarco.osgan.files.Files;
import dev.httpmarco.polocloud.api.logging.LoggerHandler;

import java.nio.file.Path;
import java.util.logging.Level;

public final class FileLoggerHandler implements LoggerHandler {

    private static final Path LOGS_PATH = Path.of("local/logs");

    public FileLoggerHandler() {
        Files.createDirectoryIfNotExists(LOGS_PATH);
    }

    @Override
    public void print(Level level, String message, Throwable throwable, Object... objects) {

    }
}
