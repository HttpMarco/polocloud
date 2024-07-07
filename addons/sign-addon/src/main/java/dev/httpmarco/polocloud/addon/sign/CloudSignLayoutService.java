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

package dev.httpmarco.polocloud.addon.sign;

import dev.httpmarco.polocloud.addon.sign.configuration.LayoutConfiguration;
import lombok.Getter;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;

@Getter
public final class CloudSignLayoutService {

    private static final Path LAYOUT_PATH = Path.of("./plugins/polocloud-signs/layout.json");
    private final LayoutConfiguration layoutConfiguration;

    @SneakyThrows
    public CloudSignLayoutService() {
        if (!Files.exists(LAYOUT_PATH)) {
            Files.createDirectory(LAYOUT_PATH.getParent());
            Files.createFile(LAYOUT_PATH);

            this.layoutConfiguration = new LayoutConfiguration();
            Files.writeString(LAYOUT_PATH, CloudSignService.SIGN_GSON.toJson(this.layoutConfiguration));
        } else {
            this.layoutConfiguration = CloudSignService.SIGN_GSON.fromJson(Files.readString(LAYOUT_PATH), LayoutConfiguration.class);
        }
    }
}
