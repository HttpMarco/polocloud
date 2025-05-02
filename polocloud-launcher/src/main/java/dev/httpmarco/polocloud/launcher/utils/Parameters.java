package dev.httpmarco.polocloud.launcher.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarFile;

/**
 * General class for all parameters
 * For type-safe use
 */
public final class Parameters {

    // system key for the current cloud system version
    public static final String VERSION = "polocloud-version";
    // path for all cloud separates components
    public static final Path LIB_DIRECTORY = Paths.get("local/libs");
    // important for the launcher process to detect the next initialized class
    public static final String BOOT_LIB = "suite";
    // required libs for the first boot process
    public static final String[] REQUIRED_LIBS = {"api", BOOT_LIB, "grpc", "instance", "common"};

    static {
        try {
            Files.createDirectories(LIB_DIRECTORY);
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + LIB_DIRECTORY);
            e.printStackTrace(System.err);
        }
    }

    public static String read(String key, Path path) {
        try (var jarFile = new JarFile(path.toString())) {
            return jarFile.getManifest().getMainAttributes().getValue(key);
        } catch (Exception exception) {
            exception.printStackTrace(System.err);
        }
        return null;
    }

    public static String version() {
        return System.getProperty(VERSION);
    }
}