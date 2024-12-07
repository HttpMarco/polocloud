package dev.httpmarco.polocloud.launcher.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * Class for manipulating classpath files
 */
@UtilityClass
public final class ClasspathUtils {

    @SneakyThrows
    public void copyClassPathFile(String fileName, String path) {
        var target = Path.of(path);

        // create dir if not exists
        target.getParent().toFile().mkdirs();

        Files.copy(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)), target, StandardCopyOption.REPLACE_EXISTING);
    }
}
