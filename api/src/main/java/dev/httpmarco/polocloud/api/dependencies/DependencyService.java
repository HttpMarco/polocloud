package dev.httpmarco.polocloud.api.dependencies;

import dev.httpmarco.polocloud.runner.RunnerBootstrap;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public final class DependencyService {

    private final Set<Dependency> dependencies = new HashSet<>();

    @SneakyThrows
    public DependencyService() {
        if (!Files.exists(RunnerBootstrap.RUNNER.dependencyFolder())) {
            Files.createDirectory(RunnerBootstrap.RUNNER.dependencyFolder());
        }

        // load default dependencies of base and instance
        this.load(new Dependency("dev.httpmarco", "osgan-utils", "1.1.19-SNAPSHOT", "1.1.19-20240521.201941-1", Dependency.MAVEN_CENTRAL_SNAPSHOT_REPO));
        this.load(new Dependency("dev.httpmarco", "osgan-files", "1.1.19-SNAPSHOT", "1.1.19-20240521.201941-1", Dependency.MAVEN_CENTRAL_SNAPSHOT_REPO));
        this.load(new Dependency("dev.httpmarco", "osgan-netty", "1.1.19-SNAPSHOT", "1.1.19-20240521.201941-1", Dependency.MAVEN_CENTRAL_SNAPSHOT_REPO));
        this.load(new Dependency("io.netty", "netty5-common", "5.0.0.Alpha5"));
        this.load(new Dependency("io.netty", "netty5-transport", "5.0.0.Alpha5"));
        this.load(new Dependency("io.netty", "netty5-codec", "5.0.0.Alpha5"));
        this.load(new Dependency("io.netty", "netty5-resolver", "5.0.0.Alpha5"));
        this.load(new Dependency("io.netty", "netty5-buffer", "5.0.0.Alpha5"));
        this.load(new Dependency("io.netty", "netty5-transport-classes-epoll", "5.0.0.Alpha5"));
        this.load(new Dependency("com.google.code.gson", "gson", "2.10.1"));
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
