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
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.util.jar.JarFile;

public final class CloudInstanceRunner implements CloudRunner {

    private static final Path LOCAL_PATH = Path.of("../../local");

    @Override
    @SneakyThrows
    public void run() {
        var commonFile = LOCAL_PATH.resolve("polocloud-common.jar");
        var apiFile = LOCAL_PATH.resolve("polocloud-api.jar");
        var instanceFile = LOCAL_PATH.resolve("polocloud-instance.jar");

        RunnerBootstrap.LOADER.addURL(commonFile.toUri().toURL());
        RunnerBootstrap.LOADER.addURL(apiFile.toUri().toURL());
        RunnerBootstrap.LOADER.addURL(instanceFile.toUri().toURL());

        RunnerBootstrap.INSTRUMENTATION.appendToSystemClassLoaderSearch(new JarFile(commonFile.toFile()));
        RunnerBootstrap.INSTRUMENTATION.appendToSystemClassLoaderSearch(new JarFile(apiFile.toFile()));
        RunnerBootstrap.INSTRUMENTATION.appendToSystemClassLoaderSearch(new JarFile(instanceFile.toFile()));
    }

    @Override
    public String mainEntry() {
        return "dev.httpmarco.polocloud.runner.CloudInstance";
    }

    @Override
    public Path dependencyFolder() {
        return LOCAL_PATH.resolve("dependencies");
    }
}
