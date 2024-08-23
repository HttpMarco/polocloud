package dev.httpmarco.polocloud.node.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@UtilityClass
public class DirectoryActions {

    @SneakyThrows
    private static boolean deleteDirectoryContents(@NotNull File directoryPath) {
        if (!directoryPath.exists() || !directoryPath.isDirectory()) {
            return false;
        }

        var files = directoryPath.listFiles();
        if (files == null) {
            return false;
        }

        boolean success = true;
        for (File file : files) {
            success &= deleteRecursively(file);
        }
        Files.deleteIfExists(directoryPath.toPath());
        return success;
    }

    private static boolean deleteRecursively(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File child : files) {
                    if (!deleteRecursively(child)) {
                        return false;
                    }
                }
            }
        }
        return file.delete();
    }

    public static void delete(@NotNull File file) {
        deleteDirectoryContents(file);
    }

    public static boolean delete(@NotNull Path file) {
        return deleteDirectoryContents(file.toFile());
    }

    @SneakyThrows
    public Path createDirectory(Path path) {
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
        return path;
    }

    public static Path createDirectory(String path) {
        return createDirectory(Path.of(path));
    }

    public static boolean copyDirectoryContents(Path sourceDirectoryPath, Path targetDirectoryPath) {
        return copyDirectoryContents(sourceDirectoryPath.toString(), targetDirectoryPath.toString());
    }

    public static boolean copyDirectoryContents(String sourceDirectoryPath, String targetDirectoryPath) {
        File sourceDirectory = new File(sourceDirectoryPath);
        File targetDirectory = new File(targetDirectoryPath);

        if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) {
            return false;
        }

        if (!targetDirectory.exists()) {
            if (!targetDirectory.mkdirs()) {
                return false;
            }
        }

        File[] files = sourceDirectory.listFiles();
        if (files == null) {
            return false;
        }

        boolean success = true;
        for (File file : files) {
            File targetFile = new File(targetDirectory, file.getName());
            if (file.isDirectory()) {
                success &= copyDirectoryContents(file.getAbsolutePath(), targetFile.getAbsolutePath());
            } else {
                success &= copyFile(file.toPath(), targetFile.toPath());
            }
        }
        return success;
    }

    private static boolean copyFile(Path source, Path target) {
        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
