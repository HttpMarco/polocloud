package dev.httpmarco.polocloud.common;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@UtilityClass
public class Downloader {

    public static @NotNull File file(String url, @NotNull Path path) {
        try (var in = new URL(url).openStream()) {
            Files.createDirectories(path.getParent());
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
            return path.toFile();
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file from " + url + " to " + path, e);
        }
    }
}