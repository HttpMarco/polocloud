package dev.httpmarco.polocloud.api.dependencies;

import dev.httpmarco.polocloud.runner.RunnerBootstrap;
import lombok.SneakyThrows;

import java.nio.file.Files;

public final class DependencyService {

    @SneakyThrows
    public DependencyService() {
        var dependenciesPath = RunnerBootstrap.RUNNER.dependencyFolder();

        if (!Files.exists(dependenciesPath)) {
            Files.createDirectory(dependenciesPath);
        }

        // todo generate default dependency definitions
        // load default dependencies of base and instance
        Dependency.load("dev.httpmarco", "osgan-utils", "1.1.19-SNAPSHOT", "1.1.19-20240521.201941-1", Dependency.MAVEN_CENTRAL_SNAPSHOT_REPO);;
        Dependency.load("dev.httpmarco", "osgan-reflections", "1.1.19-SNAPSHOT", "1.1.19-20240521.201941-1", Dependency.MAVEN_CENTRAL_SNAPSHOT_REPO);
        Dependency.load("dev.httpmarco", "osgan-files", "1.1.19-SNAPSHOT", "1.1.19-20240521.201941-1", Dependency.MAVEN_CENTRAL_SNAPSHOT_REPO);
        Dependency.load("dev.httpmarco", "osgan-netty", "1.2.1-SNAPSHOT", "1.2.1-20240525.221303-1", Dependency.MAVEN_CENTRAL_SNAPSHOT_REPO);
        Dependency.load("io.netty", "netty5-common", "5.0.0.Alpha5");
        Dependency.load("io.netty", "netty5-transport", "5.0.0.Alpha5");
        Dependency.load("io.netty", "netty5-codec", "5.0.0.Alpha5");
        Dependency.load("io.netty", "netty5-resolver", "5.0.0.Alpha5");
        Dependency.load("io.netty", "netty5-buffer", "5.0.0.Alpha5");
        Dependency.load("io.netty", "netty5-transport-classes-epoll", "5.0.0.Alpha5");
        Dependency.load("com.google.code.gson", "gson", "2.10.1");
    }
}
