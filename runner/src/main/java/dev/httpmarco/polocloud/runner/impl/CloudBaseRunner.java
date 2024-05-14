package dev.httpmarco.polocloud.runner.impl;

import dev.httpmarco.polocloud.runner.CloudRunner;
import dev.httpmarco.polocloud.runner.RunnerBoostrap;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public final class CloudBaseRunner implements CloudRunner {

    private static final Path LOCAL_PATH = Path.of("local");

    @SneakyThrows
    @Override
    public void run() {

        LOCAL_PATH.toFile().mkdirs();

        var apiFile = LOCAL_PATH.resolve("polocloud-api.jar");
        var baseFile = LOCAL_PATH.resolve("polocloud-base.jar");

        this.convertFileFromClasspath(apiFile, "api");
        this.convertFileFromClasspath(baseFile, "base");

        // add main cloud to current context classpath
        RunnerBoostrap.LOADER.addURL(apiFile.toUri().toURL());
        RunnerBoostrap.LOADER.addURL(baseFile.toUri().toURL());
    }


    @Override
    public String mainEntry() {
        return "dev.httpmarco.polocloud.base.CloudBaseBoostrap";
    }

    @Override
    public Path dependencyFolder() {
        return Path.of("local/dependencies");
    }

    @SneakyThrows
    private void convertFileFromClasspath(Path path, String fileName) {
        Files.copy(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(fileName + ".jar")), path, StandardCopyOption.REPLACE_EXISTING);
    }
}
