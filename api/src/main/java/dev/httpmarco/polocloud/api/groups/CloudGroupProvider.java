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

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import lombok.SneakyThrows;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public abstract class CloudGroupProvider {

    public abstract boolean createGroup(String name, String platform, int memory, int minOnlineCount);

    public abstract boolean deleteGroup(String name);

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

    public abstract CloudGroup fromPacket(PacketBuffer buffer);

}