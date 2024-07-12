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

import dev.httpmarco.polocloud.api.groups.CloudGroup;
import lombok.SneakyThrows;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public abstract class CloudServiceProvider {

    public abstract CloudServiceFactory factory();

    @SneakyThrows
    public List<CloudService> services() {
        return servicesAsync().get(5, TimeUnit.SECONDS);
    }

    public abstract CompletableFuture<List<CloudService>> servicesAsync();

    @SneakyThrows
    public List<CloudService> filterService(ServiceFilter filter) {
        return this.filterServiceAsync(filter).get(5, TimeUnit.SECONDS);
    }

    public abstract CompletableFuture<List<CloudService>> filterServiceAsync(ServiceFilter filter);

    @SneakyThrows
    public CompletableFuture<List<CloudService>> servicesAsync(CloudGroup group) {
        return servicesAsync(group.name());
    }

    @SneakyThrows
    public List<CloudService> services(CloudGroup group) {
        return servicesAsync(group.name()).get(5, TimeUnit.SECONDS);
    }

    @SneakyThrows
    public List<CloudService> services(String group) {
        return servicesAsync(group).get(5, TimeUnit.SECONDS);
    }

    public abstract CompletableFuture<List<CloudService>> servicesAsync(String group);

    @SneakyThrows
    public CloudService find(UUID id) {
        return this.findAsync(id).get(5, TimeUnit.SECONDS);
    }

    @SneakyThrows
    public CloudService find(String name) {
        return this.findAsync(name).get(5, TimeUnit.SECONDS);
    }

    public abstract CompletableFuture<CloudService> findAsync(String name);

    public abstract CompletableFuture<CloudService> findAsync(UUID id);

    public abstract CloudService generateService(CloudGroup parent, int orderedId, UUID id, int port, ServiceState state, String hostname, int maxMemory, int maxPlayers, String node);

}
