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

package dev.httpmarco.polocloud.base.groups.platforms;

import dev.httpmarco.polocloud.api.common.YamlValidateWriter;
import dev.httpmarco.polocloud.api.groups.platforms.PlatformVersion;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import dev.httpmarco.polocloud.runner.RunnerBootstrap;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class BungeeCordPlatform extends Platform {

    private static final String LATEST_BUNGEECORD = "https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar";

    public BungeeCordPlatform() {
        super(true);

        possibleVersions().add(new PlatformVersion("bungeecord-latest", proxy()));
    }

    @Override
    @SneakyThrows
    public void download(String version) {
        final var url = URI.create(LATEST_BUNGEECORD).toURL();
        var platformPath = Path.of("local").resolve("platforms").resolve(version);
        platformPath.toFile().mkdirs();
        Files.copy(url.openConnection().getInputStream(), Path.of(platformPath + "/server.jar"));
    }

    @Override
    @SneakyThrows
    public void prepare(@NotNull LocalCloudService localCloudService) {

        var configuration = localCloudService.runningFolder().resolve("config.yml");

        if (!Files.exists(configuration)) {
            Files.copy(Objects.requireNonNull(RunnerBootstrap.LOADER.getResourceAsStream("server-files/bungeecord/config.yml")), localCloudService.runningFolder().resolve("config.yml"));
        }

        YamlValidateWriter.validateYaml(configuration.toFile(), s -> {
            if (s.replaceAll(" ", "").startsWith("host:")) {
                return "  host: " + localCloudService.hostname() + ":" + localCloudService.port();
            } else {
                return s;
            }
        });
    }
}
