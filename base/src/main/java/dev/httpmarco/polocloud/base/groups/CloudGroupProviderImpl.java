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

package dev.httpmarco.polocloud.base.groups;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.cluster.NodeData;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.groups.CloudGroupProvider;
import dev.httpmarco.polocloud.api.groups.platforms.PlatformVersion;
import dev.httpmarco.polocloud.api.packets.general.OperationNumberPacket;
import dev.httpmarco.polocloud.api.packets.general.OperationStatePacket;
import dev.httpmarco.polocloud.api.packets.groups.*;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.base.Node;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter
@Accessors(fluent = true)
public final class CloudGroupProviderImpl extends CloudGroupProvider {

    private final List<CloudGroup> groups;
    private final CloudGroupPlatformService platformService = new CloudGroupPlatformService();
    private final CloudGroupServiceTypeAdapter groupServiceTypeAdapter = new CloudGroupServiceTypeAdapter(platformService);

    public CloudGroupProviderImpl() {

        // register group packet responders
        var transmitter = Node.instance().transmitter();
        transmitter.responder("groups-all", (properties) -> new CloudGroupCollectionPacket(groups()));
        transmitter.responder("group-find", (properties) -> new CloudGroupPacket(group(properties.getString("name"))));
        transmitter.responder("group-exist", (properties) -> new CloudGroupExistResponsePacket(isGroup(properties.getString("name"))));
        transmitter.responder("group-service-online", (properties) -> new OperationNumberPacket(group(properties.getString("name")).onlineAmount()));

        transmitter.responder("group-delete", (properties) -> new OperationStatePacket(deleteGroup(properties.getString("name"))));
        transmitter.responder("group-create", (properties) -> new OperationStatePacket(createGroup(properties.getString("name"),
                properties.getString("platform"),
                properties.getInteger("memory"),
                properties.getInteger("minOnlineCount"),
                properties.getString("node"))));

        transmitter.listen(CloudGroupDeletePacket.class, (channelTransmit, packet) -> this.deleteGroup(packet.name()));
        transmitter.listen(CloudGroupUpdatePacket.class, (channelTransmit, packet) -> {

            var group = group(packet.group().name());
            group.properties().pool().clear();
            group.properties().pool().putAll(packet.group().properties().pool());
            this.update(packet.group());
        });

        // load default groups
        this.groups = groupServiceTypeAdapter.readGroups();

        if (!groups.isEmpty()) {
            Node.instance().logger().info("Loading following groups&2: &3" + String.join("&2, &3", groups.stream().map(CloudGroup::name).toList()));
        }
    }

    //todo add reason
    @Override
    public boolean createGroup(String name, String platformVersion, int memory, int minOnlineCount, String node) {
        if (isGroup(name)) {
            return false;
        }

        if (memory <= 0) {
            return false;
        }

        if (!platformService.isValidPlatform(platformVersion)) {
            return false;
        }

        var nodeEndpoint = Node.instance().nodeProvider().node(name);
        if (nodeEndpoint == null) {
            return false;
        }

        var platform = platformService.find(platformVersion);
        var group = new CloudGroupImpl(name, Node.instance().nodeProvider().node(node).data(), new PlatformVersion(platformVersion, platform.proxy()), memory, minOnlineCount);
        this.groupServiceTypeAdapter.includeFile(group);
        this.groups.add(group);

        return true;
    }

    @Override
    public boolean deleteGroup(String name) {
        if (!isGroup(name)) {
            return false;
        }

        var group = group(name);
        CloudAPI.instance().serviceProvider().services(group).forEach(CloudService::shutdown);
        this.groupServiceTypeAdapter.excludeFile(group);
        this.groups.remove(group);
        return true;
    }

    @Override
    public boolean isGroup(String name) {
        return groups.stream().anyMatch(it -> it.name().equalsIgnoreCase(name));
    }

    @Override
    public @NotNull CompletableFuture<Boolean> isGroupAsync(String name) {
        return CompletableFuture.completedFuture(isGroup(name));
    }

    @Override
    public CloudGroup group(String name) {
        return this.groups.stream().filter(it -> it.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public @NotNull CompletableFuture<CloudGroup> groupAsync(String name) {
        return CompletableFuture.completedFuture(group(name));
    }

    @Override
    public @NotNull CompletableFuture<List<CloudGroup>> groupsAsync() {
        return CompletableFuture.completedFuture(groups);
    }

    public void update(CloudGroup cloudGroup) {
        this.groupServiceTypeAdapter.updateFile(cloudGroup);

        CloudAPI.instance().serviceProvider().services(cloudGroup).forEach(service -> {
            if (service instanceof LocalCloudService localCloudService) {
                localCloudService.channelTransmit().sendPacket(new CloudGroupUpdatePacket(cloudGroup));
            } else {
                // todo
            }
        });
    }

    public void reload() {
        this.groups.clear();
        this.groups.addAll(groupServiceTypeAdapter.readGroups());
    }

    @Contract("_, _, _, _, _ -> new")
    @Override
    public @NotNull CloudGroup fromPacket(String name, NodeData nodeData, PlatformVersion platform, int minOnlineServices, int memory) {
        return new CloudGroupImpl(name, nodeData, platform, minOnlineServices, memory);
    }
}
