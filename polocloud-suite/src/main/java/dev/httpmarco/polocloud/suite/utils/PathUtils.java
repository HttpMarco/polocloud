package dev.httpmarco.polocloud.suite.utils;

import dev.httpmarco.polocloud.suite.PolocloudSuite;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Log4j2
@UtilityClass
public final class PathUtils {

    public @NotNull Path defineDirectory(String pathUrl) {
        var path = Path.of(pathUrl);
        try {
            if(Files.notExists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            log.info(PolocloudSuite.instance().translation().get("suite.utils.path.createDirectories.failed", pathUrl));
        }
        return path;
    }

    public @NotNull Path defineDirectory(Path pathUrl) {
        return defineDirectory(pathUrl.toString());
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
            e.printStackTrace(System.err);
            return false;
        }
    }
}
