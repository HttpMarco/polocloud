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

package dev.httpmarco.polocloud.api.player;

import dev.httpmarco.polocloud.api.services.CloudService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public abstract class CloudPlayer {

    private UUID uniqueId;
    private String name;

    @Setter(AccessLevel.PUBLIC)
    private String currentServerName;
    @Setter(AccessLevel.PUBLIC)
    private String currentProxyName;

    public abstract void sendMessage(String message);

    public abstract void sendActionBar(String message);

    public abstract void sendTitle(String title, String subtitle, Integer fadeIn, Integer stay, Integer fadeOut);

    public abstract void kick(String reason);

    public abstract void connectToServer(String serverName);

    public void connectToServer(@NotNull CloudService service) {
        this.connectToServer(service.name());
    }
}
