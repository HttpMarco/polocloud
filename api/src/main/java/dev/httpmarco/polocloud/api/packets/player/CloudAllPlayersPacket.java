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

package dev.httpmarco.polocloud.api.packets.player;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.packets.CloudComponentPacketHelper;
import dev.httpmarco.polocloud.api.player.CloudPlayer;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class CloudAllPlayersPacket extends Packet {

    private List<CloudPlayer> players;

    public CloudAllPlayersPacket(List<CloudPlayer> players) {
        this.players = players;
    }

    @Override
    public void read(PacketBuffer codecBuffer) {
        this.players = new ArrayList<>();

        int amount = codecBuffer.readInt();
        for (int i = 0; i < amount; i++) {
            this.players.add(CloudComponentPacketHelper.readPlayer(codecBuffer));
        }
    }

    @Override
    public void write(PacketBuffer codecBuffer) {
        codecBuffer.writeInt(players.size());

        for (CloudPlayer player : players) {
            CloudComponentPacketHelper.writePlayer(player, codecBuffer);
        }
    }
}
