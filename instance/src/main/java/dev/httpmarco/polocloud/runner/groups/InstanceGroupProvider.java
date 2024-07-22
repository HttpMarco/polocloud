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
import dev.httpmarco.polocloud.api.cluster.NodeData;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.groups.CloudGroupProvider;
import dev.httpmarco.polocloud.api.groups.platforms.PlatformVersion;
import dev.httpmarco.polocloud.api.packets.general.OperationStatePacket;
import dev.httpmarco.polocloud.api.packets.groups.*;
import dev.httpmarco.polocloud.runner.CloudInstance;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public final class InstanceGroupProvider extends CloudGroupProvider {

    @Override
    @SneakyThrows
    public Optional<String> createGroup(String name, String platform, int memory, int minOnlineCount, String node) {
        var future = new CompletableFuture<Optional<String>>();
        CloudInstance.instance().client().request("group-create", new CommunicationProperty()
                        .set("name", name)
                        .set("node", node)
                        .set("platform", platform)
                        .set("memory", memory)
                        .set("minOnlineCount", minOnlineCount)
                , OperationStatePacket.class, it -> future.complete(it.asOptional()));
        return future.get(5, TimeUnit.SECONDS);
    }

    @Override
    @SneakyThrows
    public Optional<String> deleteGroup(String name) {
        var future = new CompletableFuture<Optional<String>>();
        CloudInstance.instance().client().request("group-delete", new CommunicationProperty().set("name", name), OperationStatePacket.class, it -> future.complete(it.asOptional()));
        return future.get(5, TimeUnit.SECONDS);
    }

    @Override
    public @NotNull CompletableFuture<Boolean> isGroupAsync(String name) {
        var future = new CompletableFuture<Boolean>();
        CloudInstance.instance().client().request("group-exist", new CommunicationProperty().set("name", name), CloudGroupExistResponsePacket.class, it -> future.complete(it.response()));
        return future;
    }

    @Override
    public @NotNull CompletableFuture<CloudGroup> groupAsync(String name) {
        var future = new CompletableFuture<CloudGroup>();
        CloudInstance.instance().client().request("group-find", new CommunicationProperty().set("name", name), CloudGroupPacket.class, it -> future.complete(it.group()));
        return future;
    }

    @Override
    public @NotNull CompletableFuture<List<CloudGroup>> groupsAsync() {
        var future = new CompletableFuture<List<CloudGroup>>();
        CloudInstance.instance().client().request("groups-all", CloudGroupCollectionPacket.class, it -> future.complete(it.groups()));
        return future;
    }

    @Override
    public void update(CloudGroup cloudGroup) {
        CloudInstance.instance().client().sendPacket(new CloudGroupUpdatePacket(cloudGroup));
    }

    @Contract("_, _, _, _, _ -> new")
    @Override
    public @NotNull CloudGroup fromPacket(String name, NodeData nodeData, PlatformVersion platform, int minOnlineServices, int memory) {
        return new InstanceGroup(name, nodeData, platform, minOnlineServices, memory);
    }
}
