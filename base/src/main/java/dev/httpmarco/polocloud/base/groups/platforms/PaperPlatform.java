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
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.groups.CloudGroupPlatformService;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import dev.httpmarco.polocloud.runner.RunnerBootstrap;
import lombok.SneakyThrows;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public final class PaperPlatform extends PaperMCPlatform {

    private static final Gson WRITER = new Gson();

    public PaperPlatform() {
        super("paper", false);
    }

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void prepare(LocalCloudService localCloudService) {
        // accept eula without cringe logs
        Files.writeString(localCloudService.runningFolder().resolve("eula.txt"), "eula=true");

        var serverProperties = localCloudService.runningFolder().resolve("server.properties").toFile();

        if (!Files.exists(serverProperties.toPath())) {
            // copy file from storage
            Files.copy(Objects.requireNonNull(RunnerBootstrap.LOADER.getResourceAsStream("server-files/spigot/server.properties")), localCloudService.runningFolder().resolve("server.properties"));
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

                            if (Files.exists(globalPaperProperty)) {
                                Yaml yaml = new Yaml();

                                var paperProperties = (Map<String, Object>) yaml.load(Files.readString(globalPaperProperty));
                                var proxyProperties = (Map<String, Object>) paperProperties.get("proxies");
                                var velocityProperties = (Map<String, Object>) proxyProperties.get("velocity");

                                velocityProperties.put("enabled", true);
                                velocityProperties.put("secret", CloudGroupPlatformService.PROXY_SECRET);

                                try {
                                    var writer = new FileWriter(globalPaperProperty.toString());
                                    yaml.dump(paperProperties, writer);
                                    writer.close();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                Map<String, Object> data = new LinkedHashMap<>();
                                Map<String, Object> proxies = new LinkedHashMap<>();
                                Map<String, Object> velocity = new LinkedHashMap<>();

                                velocity.put("enabled", true);
                                velocity.put("online-mode", true);
                                velocity.put("secret", CloudGroupPlatformService.PROXY_SECRET);
                                proxies.put("velocity", velocity);
                                data.put("proxies", proxies);

                                try {
                                    Files.createDirectories(configPath);
                                    FileWriter writer = new FileWriter(configPath.resolve("paper-global.yml").toString());

                                    writer.write(WRITER.toJson(data));
                                    writer.close();
                                } catch (IOException exception) {
                                    throw new RuntimeException(exception);
                                }
                            }
                            return;
                        }

                        if (platform instanceof BungeeCordPlatform bungeeCordPlatform) {
                            // enable bungeecord in spigot.yml
                            return;
                        }
                    }catch (Exception exception) {
                        exception.printStackTrace();
                    }
                });

    }
}
