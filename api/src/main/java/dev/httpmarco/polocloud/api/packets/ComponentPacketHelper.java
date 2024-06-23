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
import dev.httpmarco.osgan.utils.data.Pair;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.player.CloudPlayer;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.api.properties.Property;
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

        writeProperties(cloudService.properties(), buffer);
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

        // version writing
        codecBuffer.writeString(group.version().platform());
        codecBuffer.writeString(group.version().version());
        codecBuffer.writeBoolean(group.version().proxy());

        codecBuffer.writeInt(group.minOnlineService());
        codecBuffer.writeInt(group.memory());

        writeProperties(group.properties(), codecBuffer);
    }

    private static void writeProperties(PropertiesPool<?> properties, PacketBuffer codecBuffer) {
        codecBuffer.writeInt(properties.properties().size());
        properties.properties().forEach((property, o) -> writeProperty(property, o, codecBuffer));
    }

    public static void writeProperty(Property<?> property, Object value, PacketBuffer buffer) {
        buffer.writeString(property.id());

        if (value instanceof Integer intValue) {
            buffer.writeInt(intValue);
        } else if (value instanceof String stringValue) {
            buffer.writeString(stringValue);
        } else if (value instanceof Boolean booleanValue) {
            buffer.writeBoolean(booleanValue);
        }
    }

    public static Pair<Property<?>, ?> readProperty(PacketBuffer buffer) {
        var name = buffer.readString();
        var property = PropertiesPool.property(name);

        if (property.type().equals(Integer.class) || property.type().equals(int.class)) {
            return new Pair<>(property, buffer.readInt());
        } else if (property.type().equals(String.class)) {
            return new Pair<>(property, buffer.readString());
        } else if (property.type().equals(Boolean.class) || property.type().equals(boolean.class)) {
            return new Pair<>(property, buffer.readBoolean());
        }
        return null;
    }

    public static PropertiesPool<?> readProperties(PacketBuffer buffer) {
        var properties = new PropertiesPool<>();
        var elementSize = buffer.readInt();

        for (int i = 0; i < elementSize; i++) {
            var property = readProperty(buffer);
            properties.putRaw(property.getKey(), property.getValue());
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
