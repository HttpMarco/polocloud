package dev.httpmarco.polocloud.runner;

import java.nio.file.Path;

public interface CloudRunner {

    void run();

    String mainEntry();

    Path dependencyFolder();

}
