package dev.httpmarco.polocloud.api.dependencies;

import dev.httpmarco.polocloud.runner.RunnerBoostrap;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public final class DependencyService {

    private final Set<Dependency> dependencies = new HashSet<>();

    @SneakyThrows
    public DependencyService() {
        if (!Files.exists(RunnerBoostrap.RUNNER.dependencyFolder())) {
            Files.createDirectory(RunnerBoostrap.RUNNER.dependencyFolder());
        }
    }

    public void load(Dependency dependency) {
        this.dependencies.add(dependency);
    }

    public void load(Dependency... dependencies) {
        for (var dependency : dependencies) {
            this.load(dependency);
        }
    }
}
