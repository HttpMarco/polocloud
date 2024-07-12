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

import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.osgan.utils.executers.FutureResult;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.packets.service.CloudAllServicesPacket;
import dev.httpmarco.polocloud.api.packets.service.CloudServicePacket;
import dev.httpmarco.polocloud.api.services.*;
import dev.httpmarco.polocloud.runner.CloudInstance;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
@Accessors(fluent = true)
public final class InstanceServiceProvider extends CloudServiceProvider {

    private final CloudServiceFactory factory = new InstanceCloudServiceFactory();

    @Override
    public CompletableFuture<List<CloudService>> servicesAsync() {
        var future = new FutureResult<List<CloudService>>();
        CloudInstance.instance().client().transmitter().request("services-all", CloudAllServicesPacket.class, it -> future.complete(it.services()));
        return future.toCompletableFuture();
    }

    @Override
    public CompletableFuture<List<CloudService>> filterServiceAsync(ServiceFilter filter) {
        var future = new FutureResult<List<CloudService>>();
        CloudInstance.instance().client().transmitter().request("services-filtering", new CommunicationProperty().set("filter", filter), CloudAllServicesPacket.class, it -> future.complete(it.services()));
        return future.toCompletableFuture();
    }

    @Override
    public CompletableFuture<List<CloudService>> servicesAsync(String group) {
        var future = new FutureResult<List<CloudService>>();
        CloudInstance.instance().client().transmitter().request("services-group", new CommunicationProperty().set("name", group), CloudAllServicesPacket.class, it -> future.complete(it.services()));
        return future.toCompletableFuture();
    }

    @Override
    public CompletableFuture<CloudService> findAsync(String name) {
        var future = new FutureResult<CloudService>();
        CloudInstance.instance().client().transmitter().request("service-find-name", new CommunicationProperty().set("name", name), CloudServicePacket.class, it -> future.complete(it.service()));
        return future.toCompletableFuture();
    }

    @Override
    public CompletableFuture<CloudService> findAsync(UUID id) {
        var future = new FutureResult<CloudService>();
        CloudInstance.instance().client().transmitter().request("service-find-id", new CommunicationProperty().set("id", id), CloudServicePacket.class, it -> future.complete(it.service()));
        return future.toCompletableFuture();
    }

    @Contract("_, _, _, _, _, _, _, _, _ -> new")
    @Override
    public @NotNull CloudService generateService(CloudGroup parent, int orderedId, UUID id, int port, ServiceState state, String hostname, int maxMemory, int maxPlayers, String node) {
        return new InstanceCloudService(orderedId, id, port, hostname, maxMemory, state, node, parent, maxPlayers);
    }
}
