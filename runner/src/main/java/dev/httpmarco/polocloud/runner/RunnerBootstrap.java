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

package dev.httpmarco.polocloud.runner;

import dev.httpmarco.polocloud.runner.loader.CloudClassLoader;
import dev.httpmarco.polocloud.runner.impl.CloudBaseRunner;
import dev.httpmarco.polocloud.runner.impl.CloudInstanceRunner;
import lombok.SneakyThrows;

import java.lang.instrument.Instrumentation;
import java.util.Arrays;

public class RunnerBootstrap {

    public static CloudRunner RUNNER;
    public static final CloudClassLoader LOADER = new CloudClassLoader();
    public static Instrumentation INSTRUMENTATION;

    public static void premain(String args, Instrumentation instrumentation) {
        INSTRUMENTATION = instrumentation;
    }

    @SneakyThrows
    public static void main(String[] args) {
        RUNNER = Arrays.asList(args).contains("--instance") ? new CloudInstanceRunner() : new CloudBaseRunner();

        Thread.currentThread().setContextClassLoader(LOADER);

        // clone needed runtime files
        RUNNER.run();

        Class.forName(RUNNER.mainEntry(), true, LOADER).getMethod("main", String[].class).invoke(null, (Object) args);
    }
}
