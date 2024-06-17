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

import com.google.gson.Gson;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.common.YamlValidateWriter;
import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.groups.CloudGroupPlatformService;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import dev.httpmarco.polocloud.runner.RunnerBootstrap;
import lombok.SneakyThrows;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public final class PaperPlatform extends PaperMCPlatform {

    private static final Gson WRITER = new Gson();

    public PaperPlatform() {
        super("paper", false);
    }

    @Override
    @SneakyThrows
    public void prepare(LocalCloudService localCloudService) {
        // accept eula without cringe logs
        Files.writeString(localCloudService.runningFolder().resolve("eula.txt"), "eula=true");

        var serverProperties = localCloudService.runningFolder().resolve("server.properties").toFile();
        var spigotProperties = localCloudService.runningFolder().resolve("spigot.yml").toFile();

        if (!Files.exists(serverProperties.toPath())) {
            // copy file from storage
            Files.copy(Objects.requireNonNull(RunnerBootstrap.LOADER.getResourceAsStream("server-files/spigot/server.properties")), localCloudService.runningFolder().resolve("server.properties"));
        }

        if (!Files.exists(spigotProperties.toPath())) {
            // copy file from storage
            Files.copy(Objects.requireNonNull(RunnerBootstrap.LOADER.getResourceAsStream("server-files/spigot/spigot.yml")), localCloudService.runningFolder().resolve("spigot.yml"));
        }

        var properties = new Properties();

        try (var fileReader = new FileReader(serverProperties)) {
            properties.load(fileReader);
        }

        properties.setProperty("online-mode", "false");
        properties.setProperty("server-name", localCloudService.name());
        properties.setProperty("server-port", String.valueOf(localCloudService.port()));

        try (var fileWriter = new FileWriter(serverProperties)) {
            properties.store(fileWriter, null);
        }


        CloudAPI.instance().groupProvider().groups().stream()
                .filter(it -> it.platform().proxy())
                .map(it -> it.platform().version())
                .distinct()
                .forEach(platformVersion -> {
                    try {
                        var platform = CloudBase.instance().groupProvider().platformService().find(platformVersion);
                        if (platform instanceof VelocityPlatform) {
                            // manipulate velocity secret if
                            var configPath = localCloudService.runningFolder().resolve("config");
                            var globalPaperProperty = configPath.resolve("paper-global.yml");

                            if (!Files.exists(globalPaperProperty)) {
                                globalPaperProperty.toFile().getParentFile().mkdirs();
                                Files.createFile(globalPaperProperty);
                                Files.writeString(globalPaperProperty, String.join("\n", List.of("proxies:", " velocity:", "    enabled: true", "    secret: " + CloudGroupPlatformService.PROXY_SECRET)));
                            } else {
                                YamlValidateWriter.validateYaml(globalPaperProperty.toFile(), s -> {
                                    if (s.startsWith("    enabled: false")) {
                                        return "    enabled: true";
                                    }
                                    if (s.replaceAll(" ", "").startsWith("secret:")) {
                                        return "    secret: " + CloudGroupPlatformService.PROXY_SECRET;
                                    }
                                    return s;
                                });
                            }
                            return;
                        }

                        if (platform instanceof BungeeCordPlatform) {
                            var globalPaperProperty = localCloudService.runningFolder().resolve("spigot.yml");

                            YamlValidateWriter.validateYaml(globalPaperProperty.toFile(), s -> {
                                if (s.replaceAll(" ", "").startsWith("bungeecord:")) {
                                    return "  bungeecord: true";
                                } else {
                                    return s;
                                }
                            });
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                });

    }
}
