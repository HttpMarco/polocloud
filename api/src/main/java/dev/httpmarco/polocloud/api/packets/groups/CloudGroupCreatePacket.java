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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class CloudGroupCreatePacket extends Packet {

    private String name;
    private String platform;
    private String version;
    private int memory;
    private int minOnlineCount;

    @Override
    public void read(PacketBuffer packetBuffer) {
        this.name = packetBuffer.readString();
        this.platform = packetBuffer.readString();
        this.version = packetBuffer.readString();
        this.memory = packetBuffer.readInt();
        this.minOnlineCount = packetBuffer.readInt();
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeString(name);
        packetBuffer.writeString(platform);
        packetBuffer.writeString(version);
        packetBuffer.writeInt(memory);
        packetBuffer.writeInt(minOnlineCount);
    }
}
