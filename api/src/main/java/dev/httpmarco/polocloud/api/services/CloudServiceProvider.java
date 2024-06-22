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

package dev.httpmarco.polocloud.api.services;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import lombok.SneakyThrows;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public interface CloudServiceProvider {

    CloudServiceFactory factory();

    List<CloudService> services();

    CompletableFuture<List<CloudService>> servicesAsync();

    @SneakyThrows
    default List<CloudService> filterService(ServiceFilter filter) {
        return this.filterServiceAsync(filter).get(5, TimeUnit.SECONDS);
    }

    CompletableFuture<List<CloudService>> filterServiceAsync(ServiceFilter filter);

    List<CloudService> services(CloudGroup group);

    CloudService find(UUID id);

    CloudService find(String name);

    CompletableFuture<CloudService> findAsync(String name);

    CompletableFuture<CloudService> findAsync(UUID id);

    CloudService service(String name);

    CloudService generateService(CloudGroup parent, int orderedId, UUID id, int port, ServiceState state, String hostname, int maxMemory, int maxPlayers);

}
