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

package dev.httpmarco.polocloud.proxy.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;

public class Config {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path LAYOUT_PATH = Path.of("./plugins/PoloCloud-Proxy/config.json");

    @SneakyThrows
    public Config() {
        if (!Files.exists(LAYOUT_PATH)) {
            Files.createDirectory(LAYOUT_PATH.getParent());
            Files.createFile(LAYOUT_PATH);
        }
    }

    @SneakyThrows
    public static <T> T load(Class<T> clazz) {
        return GSON.fromJson(Files.readString(LAYOUT_PATH), clazz);
    }

    @SneakyThrows
    public static <T> void save(T config) {
        Files.writeString(LAYOUT_PATH, GSON.toJson(config));
    }
}
