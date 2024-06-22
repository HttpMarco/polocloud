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

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.player.CloudPlayer;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.api.services.ServiceState;

public final class ComponentPacketHelper {

    public static void writeService(CloudService cloudService, PacketBuffer buffer) {
        writeGroup(cloudService.group(), buffer);

        buffer.writeInt(cloudService.orderedId())
                .writeUniqueId(cloudService.id())
                .writeInt(cloudService.port())
                .writeEnum(cloudService.state())
                .writeString(cloudService.hostname())
                .writeInt(cloudService.memory())
                .writeInt(cloudService.maxPlayers());

        writeProperty(cloudService.properties(), buffer);
    }

    public static CloudService readService(PacketBuffer buffer) {

        var group = readGroup(buffer);
        var orderedId = buffer.readInt();
        var id = buffer.readUniqueId();
        var port = buffer.readInt();
        var state = buffer.readEnum(ServiceState.class);
        var hostname = buffer.readString();
        var maxMemory = buffer.readInt();
        var maxPlayers = buffer.readInt();

        var service = CloudAPI.instance().serviceProvider().generateService(group, orderedId, id, port, state, hostname, maxMemory, maxPlayers);
        service.properties().appendAll(readProperties(buffer));
        return service;
    }

    public static void writeGroup(CloudGroup group, PacketBuffer codecBuffer) {
        codecBuffer.writeString(group.name());
        codecBuffer.writeString(group.platform().version());
        codecBuffer.writeBoolean(group.platform().proxy());
        codecBuffer.writeInt(group.minOnlineServices());
        codecBuffer.writeInt(group.memory());

        writeProperty(group.properties(), codecBuffer);
    }

    private static void writeProperty(PropertiesPool<?> properties, PacketBuffer codecBuffer) {
        codecBuffer.writeInt(properties.properties().size());
        properties.properties().forEach((property, o) -> {
            codecBuffer.writeString(property.id());

            if (o instanceof Integer intValue) {
                codecBuffer.writeInt(intValue);
            } else if (o instanceof String stringValue) {
                codecBuffer.writeString(stringValue);
            } else if (o instanceof Boolean booleanValue) {
                codecBuffer.writeBoolean(booleanValue);
            }
        });
    }

    public static PropertiesPool<?> readProperties(PacketBuffer buffer) {
        var properties = new PropertiesPool<>();
        var elementSize = buffer.readInt();

        for (int i = 0; i < elementSize; i++) {

            var name = buffer.readString();
            var property = PropertiesPool.property(name);

            if (property.type().equals(Integer.class) || property.type().equals(int.class)) {
                properties.putRaw(property, buffer.readInt());
            } else if (property.type().equals(String.class)) {
                properties.putRaw(property, buffer.readString());
            } else if (property.type().equals(Boolean.class) || property.type().equals(boolean.class)) {
                properties.putRaw(property, buffer.readBoolean());
            }
        }

        return properties;
    }

    public static CloudGroup readGroup(PacketBuffer codecBuffer) {
        var group = CloudAPI.instance().groupProvider().fromPacket(codecBuffer);

        group.properties().appendAll(readProperties(codecBuffer));
        return group;
    }

    public static void writePlayer(CloudPlayer cloudPlayer, PacketBuffer codecBuffer) {
        codecBuffer.writeUniqueId(cloudPlayer.uniqueId());
        codecBuffer.writeString(cloudPlayer.name());
        codecBuffer.writeString(cloudPlayer.currentServerName());
        codecBuffer.writeString(cloudPlayer.currentProxyName());
    }

    public static CloudPlayer readPlayer(PacketBuffer codecBuffer) {
        return CloudAPI.instance().playerProvider().fromPacket(codecBuffer);
    }
}
