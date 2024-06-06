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
import dev.httpmarco.polocloud.api.groups.platforms.PlatformVersion;
import dev.httpmarco.polocloud.base.groups.CloudGroupPlatformService;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import lombok.SneakyThrows;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public final class BungeeCordPlatform extends Platform {

    private static final Gson WRITER = new Gson();
    private static final String LATEST_BUNGEECORD = "https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar";

    public BungeeCordPlatform() {
        super(true);

        possibleVersions().add(new PlatformVersion("bungeecord-latest", proxy()));
    }

    @Override
    @SneakyThrows
    public void download(String version) {
        final var url = URI.create(LATEST_BUNGEECORD).toURL();
        //todo duplicated code
        var platformPath = Path.of("local").resolve("platforms").resolve(version);
        platformPath.toFile().mkdirs();

        Files.copy(url.openConnection().getInputStream(), Path.of(platformPath + "/server.jar"));
    }

    @Override
    public void prepare(LocalCloudService localCloudService) {

        var configuration = localCloudService.runningFolder().resolve("config.yml");

        if (Files.exists(configuration)) {
            Yaml yaml = new Yaml();

            var paperProperties = (Map<String, Object>) yaml.load(configuration.toString());
            var proxyProperties = (Collection<Map<String, Object>>) paperProperties.get("listeners");

            proxyProperties.add(Map.of("host", localCloudService.hostname() + ":" + localCloudService.port()));

            try {
                var writer = new FileWriter(paperProperties.toString());
                yaml.dump(paperProperties, writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Map<String, Object> data = new LinkedHashMap<>();
            Collection<Map<String, Object>> proxies = new ArrayList<>();

            proxies.add(Map.of("host", localCloudService.hostname() + ":" + localCloudService.port()));
            data.put("listeners", proxies);

            try {
                FileWriter writer = new FileWriter(configuration.toString());

                writer.write(WRITER.toJson(data));
                writer.close();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}
