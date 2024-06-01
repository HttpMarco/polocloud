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
import dev.httpmarco.polocloud.api.player.CloudPlayer;
import dev.httpmarco.polocloud.api.services.CloudService;

public final class CloudComponentPacketHelper {

    public static void writeService(CloudService cloudService, CodecBuffer codecBuffer) {
        writeGroup(cloudService.group(), codecBuffer);

        codecBuffer.writeInt(cloudService.orderedId());
        codecBuffer.writeUniqueId(cloudService.id());
        codecBuffer.writeInt(cloudService.port());
        codecBuffer.writeEnum(cloudService.state());
        codecBuffer.writeString(cloudService.hostname());
        //todo properties
    }

    public static CloudService readService(CodecBuffer codecBuffer) {
        //todo properties
        return CloudAPI.instance().serviceProvider().fromPacket(readGroup(codecBuffer), codecBuffer);
    }

    public static void writeGroup(CloudGroup group, CodecBuffer codecBuffer) {
        codecBuffer.writeString(group.name());
        codecBuffer.writeString(group.platform().version());
        codecBuffer.writeBoolean(group.platform().proxy());
        codecBuffer.writeInt(group.minOnlineServices());
        codecBuffer.writeInt(group.memory());

        //todo properties
    }

    public static CloudGroup readGroup(CodecBuffer codecBuffer) {
        //todo properties
        return CloudAPI.instance().groupProvider().fromPacket(codecBuffer);
    }

    public static void writePlayer(CloudPlayer cloudPlayer, CodecBuffer codecBuffer) {
        codecBuffer.writeUniqueId(cloudPlayer.uniqueId());
        codecBuffer.writeString(cloudPlayer.name());
        codecBuffer.writeString(cloudPlayer.currentServer());
        codecBuffer.writeString(cloudPlayer.currentProxy());
    }

    public static CloudPlayer readPlayer(CodecBuffer codecBuffer) {
        return CloudAPI.instance().playerProvider().fromPacket(codecBuffer);
    }
}
