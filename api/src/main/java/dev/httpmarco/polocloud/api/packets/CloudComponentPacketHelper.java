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

package dev.httpmarco.polocloud.api.packets;

import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.services.CloudService;

public final class CloudComponentPacketHelper {

    public static void writeService(CloudService cloudService, CodecBuffer buffer) {
        writeGroup(cloudService.group(), buffer);

        buffer.writeInt(cloudService.orderedId());
        buffer.writeUniqueId(cloudService.id());
        buffer.writeInt(cloudService.port());
        buffer.writeEnum(cloudService.state());
        buffer.writeString(cloudService.hostname());
        //todo properties
    }

    public static CloudService readService(CodecBuffer buffer) {
        //todo properties
        return CloudAPI.instance().serviceProvider().fromPacket(readGroup(buffer), buffer);
    }

    public static void writeGroup(CloudGroup group, CodecBuffer buffer) {
        buffer.writeString(group.name());
        buffer.writeString(group.platform().version());
        buffer.writeBoolean(group.platform().proxy());
        buffer.writeInt(group.minOnlineServices());
        buffer.writeInt(group.memory());

        //todo properties
    }

    public static CloudGroup readGroup(CodecBuffer buffer) {
        //todo properties
        return CloudAPI.instance().groupProvider().fromPacket(buffer);
    }
}
