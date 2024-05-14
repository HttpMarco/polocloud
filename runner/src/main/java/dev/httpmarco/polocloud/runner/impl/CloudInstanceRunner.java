package dev.httpmarco.polocloud.runner.impl;

import dev.httpmarco.polocloud.runner.CloudRunner;
import lombok.SneakyThrows;

import java.nio.file.Path;

public final class CloudInstanceRunner implements CloudRunner {

    @Override
    @SneakyThrows
    public void run() {

    }

    @Override
    public String mainEntry() {
        return "dev.httpmarco.polocloud.base.CloudBase";
    }

    @Override
    public Path dependencyFolder() {
        return null;
    }
}
