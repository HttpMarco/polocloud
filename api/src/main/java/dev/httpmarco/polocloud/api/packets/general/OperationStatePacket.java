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

package dev.httpmarco.polocloud.api.packets.general;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class OperationStatePacket extends Packet {

    private Boolean response;
    private String reason;

    public OperationStatePacket(@NotNull Optional<String> result) {
        this.response = result.isEmpty();

        if (!response) {
            this.reason = result.get();
        }
    }

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        this.response = packetBuffer.readBoolean();

        if (!response) {
            reason = packetBuffer.readString();
        }
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeBoolean(this.response);

        if (!response) {
            packetBuffer.writeString(reason);
        }
    }

    public Optional<String> asOptional() {
        return (response ? Optional.empty() : Optional.of(reason));
    }
}
