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

import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.groups.CloudGroupProvider;
import dev.httpmarco.polocloud.api.groups.platforms.PlatformVersion;
import dev.httpmarco.polocloud.api.packets.service.CloudServiceRegisterPacket;
import dev.httpmarco.polocloud.runner.CloudInstance;

import java.util.List;

public final class InstanceGroupProvider implements CloudGroupProvider {

    @Override
    public boolean createGroup(String name, String platform, int memory, int minOnlineCount) {
        return false;
    }

    @Override
    public boolean deleteGroup(String name) {
        return false;
    }

    @Override
    public boolean isGroup(String name) {
        return false;
    }

    @Override
    public CloudGroup group(String name) {
        return null;
    }

    @Override
    public List<CloudGroup> groups() {
        CloudInstance.instance().client().transmitter().request("groups-all", CloudServiceRegisterPacket.class, it -> {

        });
        return List.of();
    }

    @Override
    public void update(CloudGroup cloudGroup) {

    }

    @Override
    public CloudGroup fromPacket(CodecBuffer buffer) {
        var name = buffer.readString();
        var platform = buffer.readString();
        var platformProxy = buffer.readBoolean();
        var minOnlineServices = buffer.readInt();
        var memory = buffer.readInt();

        return new InstanceGroup(name, new PlatformVersion(platform, platformProxy), minOnlineServices, memory);
    }
}
