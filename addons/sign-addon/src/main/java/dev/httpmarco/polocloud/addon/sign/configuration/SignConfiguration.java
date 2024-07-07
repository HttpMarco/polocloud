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

package dev.httpmarco.polocloud.addon.sign.configuration;

import dev.httpmarco.polocloud.addon.sign.CloudSign;
import dev.httpmarco.polocloud.addon.sign.CloudSignService;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class SignConfiguration {

    private static final Path SIGN_PATH = Path.of("./plugins/polocloud-signs/signs.json");
    private final List<CloudSign> signs = new ArrayList<>();

    @SneakyThrows
    public void update() {
        Files.writeString(SIGN_PATH, CloudSignService.SIGN_GSON.toJson(this));
    }

    @SneakyThrows
    public void create() {
        if (!Files.exists(SIGN_PATH)) {

            if (!Files.exists(SIGN_PATH.getParent())) {
                Files.createDirectory(SIGN_PATH.getParent());
            }
            Files.createFile(SIGN_PATH);
            this.update();
        }
        this.signs.addAll(CloudSignService.SIGN_GSON.fromJson(Files.readString(SIGN_PATH), SignConfiguration.class).signs);
    }
}
