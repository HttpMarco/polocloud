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
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.groups.platforms.PlatformVersion;
import dev.httpmarco.polocloud.api.packets.general.OperationNumberPacket;
import dev.httpmarco.polocloud.runner.CloudInstance;

import java.util.concurrent.CompletableFuture;

public class InstanceGroup extends CloudGroup {

    public InstanceGroup(String name, PlatformVersion platform, int memory, int minOnlineService) {
        super(name, platform, memory, minOnlineService);
    }

    @Override
    public CompletableFuture<Integer> onlineAmountAsync() {
        var future = new CompletableFuture<Integer>();
        CloudInstance.instance().client().transmitter().request("group-service-online", new CommunicationProperty().set("name", name()), OperationNumberPacket.class, it -> future.complete(it.response()));
        return future;
    }

    @Override
    public void update() {
        //todo
    }
}
