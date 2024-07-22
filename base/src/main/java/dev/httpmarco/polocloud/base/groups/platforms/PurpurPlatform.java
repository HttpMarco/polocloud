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

import com.google.gson.JsonObject;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.platforms.PlatformVersion;
import dev.httpmarco.polocloud.base.Node;
import dev.httpmarco.polocloud.base.groups.CloudGroupPlatformService;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import dev.httpmarco.polocloud.runner.RunnerBootstrap;
import dev.httpmarco.pololcoud.common.GsonUtils;
import dev.httpmarco.pololcoud.common.files.FileManipulator;
import lombok.SneakyThrows;

import java.io.FileReader;
import java.io.FileWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public final class PurpurPlatform extends Platform {
    private static final String VERSION_URL = "https://api.purpurmc.org/v2/purpur/";
    private static final String DOWNLOAD_URL = "https://api.purpurmc.org/v2/purpur/%s/latest/download";

    public PurpurPlatform() {
        super(false);
        for (var version : readPurpurInformation(VERSION_URL).get("versions").getAsJsonArray()) {
            possibleVersions().add(new PlatformVersion("purpur" + "-" + version.getAsString(), false));
        }
    }

    @Override
    @SneakyThrows
    public void download(String version) {
        final var url = URI.create(DOWNLOAD_URL.formatted(version.split("-")[1])).toURL();

        var platformPath = Path.of("local").resolve("platforms").resolve(version);
        platformPath.toFile().mkdirs();

        Files.copy(url.openConnection().getInputStream(), Path.of(platformPath + "/server.jar"));
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
                        var platform = Node.instance().groupProvider().platformService().find(platformVersion);
                        if (platform instanceof VelocityPlatform) {
                            // manipulate velocity secret if
                            var configPath = localCloudService.runningFolder().resolve("config");
                            var globalPaperProperty = configPath.resolve("paper-global.yml");

                            if (!Files.exists(globalPaperProperty)) {
                                globalPaperProperty.toFile().getParentFile().mkdirs();
                                Files.createFile(globalPaperProperty);
                                Files.writeString(globalPaperProperty, String.join("\n", List.of("proxies:", " velocity:", "    enabled: true", "    secret: " + CloudGroupPlatformService.PROXY_SECRET)));
                            } else {
                                FileManipulator.manipulate(globalPaperProperty.toFile(), s -> {
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

                            FileManipulator.manipulate(globalPaperProperty.toFile(), s -> {
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


    @Override
    public String[] platformsArguments() {
        return new String[]{"nogui", "noconsole"};
    }

    @SneakyThrows
    private JsonObject readPurpurInformation(String link) {
        return GsonUtils.GSON.fromJson(downloadStringContext(link), JsonObject.class);
    }

    @SneakyThrows
    private String downloadStringContext(String link) {
        var url = new URI(link).toURL();
        var stream = url.openStream();
        var context = new String(stream.readAllBytes());
        stream.close();
        return context;
    }
}
