package dev.httpmarco.polocloud.launcher.update;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@UtilityClass
public class AutoUpdateInstaller {

    private static final Path TEMP_DIR = Path.of("local/temp/");

    @SneakyThrows
    public static void installUpdate(File oldFile, File newFile) {
        UpdateShutdownController.shutdown();
        createTempFile();

        try (var currentJar = new JarFile(oldFile)) {
            unpackJar(newFile);

            currentJar.stream()
                    .filter(entry -> !entry.getName().startsWith("dev"))
                    .forEach(entry -> {
                        try {
                            deleteEntryFromJar(currentJar, entry);
                        } catch (IOException e) {
                            System.err.printf("Failed to delete entry: %s - %s%n", entry.getName(), e.getMessage());
                        }
                    });
        } finally {
            /*Files.walk(TEMP_DIR)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::deleteOnExit);*/
        }
    }

    private void deleteEntryFromJar(JarFile jarFile, JarEntry entry) throws IOException {

    }

    @SneakyThrows
    private void unpackJar(File file) {
        try (var jarFile = new JarFile(file)) {
            jarFile.stream()
                    .filter(entry -> !entry.getName().startsWith("dev"))
                    .forEach(entry -> handleJarEntry(entry, jarFile));
        }
    }

    private void handleJarEntry(JarEntry entry, JarFile jarFile) {
        try {
            var entryDestination = TEMP_DIR.resolve(entry.getName());

            if (entry.isDirectory()) {
                Files.createDirectories(entryDestination);
            } else {
                Files.createDirectories(entryDestination.getParent());
                Files.copy(jarFile.getInputStream(entry), entryDestination, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            System.err.println("Failed to unpack jar entry: " + entry.getName() + " - " + e.getMessage());
        }
    }

    @SneakyThrows
    private void createTempFile() {
        if (!Files.exists(TEMP_DIR)) {
            Files.createDirectories(TEMP_DIR);
        }
    }
}
