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

package dev.httpmarco.polocloud.runner.services;

import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.player.CloudPlayer;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.api.services.ServiceState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class InstanceCloudService implements CloudService {

    private final int orderedId;
    private final UUID id;

    private final int port;
    private final String hostname;
    private final int memory;

    private final ServiceState state;
    private CloudGroup group;

    @Override
    public List<String> log() {
        //todo
        return List.of();
    }

    @Override
    public int currentMemory() {
        //todo
        return 0;
    }

    @Override
    public int maxPlayers() {
        //todo
        return 0;
    }

    @Override
    public int onlinePlayersCount() {
        // todo
        return 0;
    }

    @Override
    public void execute(String command) {
        //todo
    }

    @Override
    public List<CloudPlayer> onlinePlayers() {
        //todo
        return List.of();
    }

    @Override
    public PropertiesPool<?> properties() {
        return null;
    }
}
