package dev.httpmarco.polocloud.launcher.lib;

import dev.httpmarco.polocloud.launcher.utils.Parameters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Lib {

    private final String name;
    private final Path path;

    private Lib(String name) {
        this.name = name;
        this.path = Paths.get("polocloud-%s-%s.jar".formatted(name, Parameters.version()));
    }

    public static LinkedList<Lib> of(String... names) {
        return Arrays.stream(names).map(Lib::new).collect(Collectors.toCollection(LinkedList::new));
    }

    public Path target() {
        return Parameters.LIB_DIRECTORY.resolve(path);
    }

    public String mainClass() {
        return Parameters.read("Main-Class", target());
    }

    public void copyFromClasspath() {
        try {
            Files.copy(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream(path.toString())), target(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Failed to copy polocloud library from classpath: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String name() {
        return name;
    }
}
