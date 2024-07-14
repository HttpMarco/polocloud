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
import dev.httpmarco.polocloud.api.packets.service.CloudServiceShutdownPacket;
import dev.httpmarco.polocloud.api.packets.service.CloudServiceStartPacket;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.api.services.CloudServiceFactory;
import dev.httpmarco.polocloud.runner.CloudInstance;

public final class InstanceCloudServiceFactory implements CloudServiceFactory {

    @Override
    public void start(CloudGroup cloudGroup) {
        CloudInstance.instance().client().sendPacket(new CloudServiceStartPacket(cloudGroup.name()));
    }

    @Override
    public void stop(CloudService service) {
        CloudInstance.instance().client().sendPacket(new CloudServiceShutdownPacket(service.id()));
    }
}
