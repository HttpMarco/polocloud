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

package dev.httpmarco.polocloud.base.player;

import dev.httpmarco.polocloud.api.player.CloudPlayer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class CloudPlayerImpl implements CloudPlayer {

    private final UUID uniqueId;
    private final String name;

    public CloudPlayerImpl(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;
    }

    @Setter
    private String currentServerName = "null";
    @Setter
    private String currentProxyName = "null";

    @Override
    public void sendMessage(String message) {
    }

    @Override
    public void sendActionBar(String message) {
    }

    @Override
    public void sendTitle(String title, String subtitle, Integer fadeIn, Integer stay, Integer fadeOut) {
    }

    @Override
    public void kick(String reason) {
    }

    @Override
    public void connectToServer(String serverName) {
    }
}
