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

package dev.httpmarco.polocloud;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.common.CloudMemoryCalculator;
import dev.httpmarco.polocloud.api.events.service.CloudServiceOnlineEvent;
import dev.httpmarco.polocloud.api.events.service.CloudServiceShutdownEvent;
import dev.httpmarco.polocloud.api.packets.general.OperationDoublePacket;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.api.services.ServiceFilter;
import dev.httpmarco.polocloud.api.services.ServiceState;
import dev.httpmarco.polocloud.runner.CloudInstance;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.function.Consumer;

@Getter
@Accessors(fluent = true)
public class RunningProxyPlatform extends RunningPlatform {

    public RunningProxyPlatform(Consumer<CloudService> registerService, Consumer<CloudService> unregisterService) {
        var instance = CloudAPI.instance();

        CloudAPI.instance().serviceProvider().filterServiceAsync(ServiceFilter.SERVERS).whenComplete((cloudServices, throwable) -> {
            for (var service : cloudServices) {
                if (service.state() == ServiceState.ONLINE) {
                    registerService.accept(service);
                }
            }
        });

        instance.globalEventNode().addListener(CloudServiceOnlineEvent.class, event -> {
            if (event.cloudService().group().version().proxy()) {
                return;
            }
            registerService.accept(event.cloudService());
        });

        instance.globalEventNode().addListener(CloudServiceShutdownEvent.class, event -> unregisterService.accept(event.cloudService()));
    }
}
