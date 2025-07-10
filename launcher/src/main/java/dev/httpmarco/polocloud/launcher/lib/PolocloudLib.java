package dev.httpmarco.polocloud.launcher.lib;

import dev.httpmarco.polocloud.launcher.PolocloudParameters;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;

public class PolocloudLib {

    private final String name;
    private final Path path;

    public PolocloudLib(String name) {
        this.name = name;
        this.path = Paths.get(String.format("polocloud-%s-%s.jar", name, PolocloudParameters.polocloudVersion()));
    }

    public static List<PolocloudLib> of(String... names) {
        return of(null, names);
    }

    /**
     * Creates a list of PolocloudLib instances from the provided names.
     */
    public static List<PolocloudLib> of(String suffix, String... names) {
        List<PolocloudLib> libs = new ArrayList<>();
        for (String name : names) {
            libs.add(new PolocloudLib(name + (suffix != null ? "-" + suffix : "")));
        }
        return libs;
    }

    /**
     * Returns the path of the target file in the lib directory.
     */
    public Path target() {
        return PolocloudParameters.LIB_DIRECTORY.resolve(path);
    }

    /**
     * Reads the manifest of the Polocloud library and returns the value for the specified key.
     */
    public String mainClass() {
        return PolocloudParameters.readManifest("Main-Class", target());
    }

    /**
     * Copies the Polocloud library from the classpath to the target directory.
     */
    public void copyFromClasspath() {
        try (InputStream in = Objects.requireNonNull(
                ClassLoader.getSystemClassLoader().getResourceAsStream(path.toString()))) {
            Files.copy(in, target(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Failed to copy polocloud library from classpath: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String name() {
        return name;
    }
}
