package dev.httpmarco.polocloud.launcher.dependencies;

import dev.httpmarco.polocloud.launcher.PolocloudParameters;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
public final class DependencyProvider {

    @Getter
    private final List<Dependency> dependencies;

    @SneakyThrows
    public DependencyProvider() {
        Files.createDirectories(PolocloudParameters.DEPENDENCY_DIRECTORY);

        this.dependencies = List.of(loadDependencyList());
    }

    public void download() {
        this.dependencies.stream().parallel().forEach(Dependency::download);
    }

    private Dependency[] loadDependencyList() throws IOException {
        var inputStream = getClass().getResourceAsStream("/dependencies.blob");

        if (inputStream == null) {
            throw new RuntimeException("Dependency list not found in classpath");
        }

        var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        var dependencies = new ArrayList<Dependency>();
        String line;
        while ((line = reader.readLine()) != null) {
            var parts = line.split(";", -1);
            if (parts.length < 6) continue;
            var dependency = new Dependency(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
            dependencies.add(dependency);
        }

        return dependencies.toArray(new Dependency[0]);
    }
}
