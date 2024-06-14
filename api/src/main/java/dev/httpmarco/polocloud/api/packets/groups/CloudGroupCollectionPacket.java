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

package dev.httpmarco.polocloud.api.packets.groups;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.packets.ComponentPacketHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class CloudGroupCollectionPacket extends Packet {

    private List<CloudGroup> groups;

    @Override
    public void read(PacketBuffer packetBuffer) {
        var groupAmount = packetBuffer.readInt();
        var groups = new ArrayList<CloudGroup>();

        for (int i = 0; i < groupAmount; i++) {
            groups.add(ComponentPacketHelper.readGroup(packetBuffer));
        }
        this.groups = groups;
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(groups.size());
        for (var group : groups) {
            ComponentPacketHelper.writeGroup(group, packetBuffer);
        }
    }
}
