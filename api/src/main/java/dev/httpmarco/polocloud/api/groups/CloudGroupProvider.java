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

package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.polocloud.api.cluster.NodeData;
import dev.httpmarco.polocloud.api.groups.platforms.PlatformVersion;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public abstract class CloudGroupProvider {

    public abstract Optional<String> createGroup(String name, String platform, int memory, int minOnlineCount, String node);

    public abstract Optional<String> deleteGroup(String name);

    @SneakyThrows
    public boolean isGroup(String name) {
        return isGroupAsync(name).get(5, TimeUnit.SECONDS);
    }

    public abstract CompletableFuture<Boolean> isGroupAsync(String name);

    @SneakyThrows
    public CloudGroup group(String name) {
        return this.groupAsync(name).get(5, TimeUnit.SECONDS);
    }

    public abstract CompletableFuture<CloudGroup> groupAsync(String name);

    @SneakyThrows
    public List<CloudGroup> groups() {
        return this.groupsAsync().get(5, TimeUnit.SECONDS);
    }

    public abstract CompletableFuture<List<CloudGroup>> groupsAsync();

    public abstract void update(CloudGroup cloudGroup);

    public abstract CloudGroup fromPacket(String name, NodeData nodeData, PlatformVersion platform, int minOnlineServices, int memory);

}