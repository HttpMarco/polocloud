/*
 * Copyright 2024 Mirco Lindenau | HttpMarco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.httpmarco.polocloud.runner.impl;

import dev.httpmarco.polocloud.runner.CloudRunner;
import dev.httpmarco.polocloud.runner.RunnerBootstrap;
import dev.httpmarco.polocloud.runner.dependencies.Dependency;
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

        var commonFile = LOCAL_PATH.resolve("polocloud-common.jar");
        var apiFile = LOCAL_PATH.resolve("polocloud-api.jar");
        var baseFile = LOCAL_PATH.resolve("polocloud-base.jar");
        var instanceFile = LOCAL_PATH.resolve("polocloud-instance.jar");
        var pluginFile = LOCAL_PATH.resolve("polocloud-plugin.jar");

        this.convertFileFromClasspath(commonFile, "common");
        this.convertFileFromClasspath(apiFile, "api");
        this.convertFileFromClasspath(baseFile, "base");
        this.convertFileFromClasspath(instanceFile, "instance");
        this.convertFileFromClasspath(pluginFile, "plugin");

        // add main cloud to current context classpath
        RunnerBootstrap.LOADER.addURL(commonFile.toUri().toURL());
        RunnerBootstrap.LOADER.addURL(apiFile.toUri().toURL());
        RunnerBootstrap.LOADER.addURL(baseFile.toUri().toURL());


        Dependency.load("com.google.code.gson", "gson", "2.10.1");
        Dependency.load("org.jline", "jline", "3.26.1");
        Dependency.load("org.fusesource.jansi", "jansi", "2.4.1");
    }


    @Override
    public String mainEntry() {
        return "dev.httpmarco.polocloud.base.launcher.NodeLauncher";
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
