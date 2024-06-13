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

package dev.httpmarco.polocloud.runner.groups;

import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.groups.CloudGroupProvider;
import dev.httpmarco.polocloud.api.groups.platforms.PlatformVersion;
import dev.httpmarco.polocloud.api.packets.groups.CloudGroupCollectionPacket;
import dev.httpmarco.polocloud.api.packets.groups.CloudGroupPacket;
import dev.httpmarco.polocloud.runner.CloudInstance;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class InstanceGroupProvider extends CloudGroupProvider {

    @Override
    public boolean createGroup(String name, String platform, int memory, int minOnlineCount) {
        //todo
        return false;
    }

    @Override
    public boolean deleteGroup(String name) {
        //todo
        return false;
    }

    @Override
    public CompletableFuture<Boolean> isGroupAsync(String name) {
        //todo
        return null;
    }

    @Override
    public CompletableFuture<CloudGroup> groupAsync(String name) {
        var future = new CompletableFuture<CloudGroup>();
        CloudInstance.instance().client().transmitter().request("group-find", new CommunicationProperty().set("name", name), CloudGroupPacket.class, it -> future.complete(it.group()));
        return future;
    }

    @Override
    public CompletableFuture<List<CloudGroup>> groupsAsync() {
        var future = new CompletableFuture<List<CloudGroup>>();
        CloudInstance.instance().client().transmitter().request("groups-all", CloudGroupCollectionPacket.class, it -> future.complete(it.groups()));
        return future;
    }

    @Override
    public void update(CloudGroup cloudGroup) {
        //todo
    }

    @Override
    public CloudGroup fromPacket(PacketBuffer buffer) {
        var name = buffer.readString();
        var platform = buffer.readString();
        var platformProxy = buffer.readBoolean();
        var minOnlineServices = buffer.readInt();
        var memory = buffer.readInt();

        return new InstanceGroup(name, new PlatformVersion(platform, platformProxy), minOnlineServices, memory);
    }
}
