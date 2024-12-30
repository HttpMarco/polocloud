package dev.httpmarco.polocloud.launcher.utils;

import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class FileCopyBuilder {

    private String targetFolder;
    private String prefix = "";
    private String suffix = "";
    private String[] files;

    public FileCopyBuilder targetFolder(String targetFolder) {
        this.targetFolder = targetFolder;
        return this;
    }

    public FileCopyBuilder prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public FileCopyBuilder suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public FileCopyBuilder files(String... files) {
        this.files = files;
        return this;
    }

    @SneakyThrows
    public void copy() {
        if (targetFolder == null || targetFolder.isEmpty()) {
            throw new IllegalArgumentException("Target folder must be specified.");
        }

        var path = Paths.get(targetFolder);

        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }

        for (var file : files) {
            var modifiedFile = modifyFileName(file);
            var target = path.resolve(modifiedFile);
            Files.copy(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream(file)), target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Contract(pure = true)
    private @NotNull String modifyFileName(String file) {
        return prefix + file + suffix;
    }

    @Contract(" -> new")
    @SneakyThrows
    public static @NotNull FileCopyBuilder builder() {
        return new FileCopyBuilder();
    }
}
