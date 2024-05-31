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

package dev.httpmarco.polocloud.api.packets.event;

import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.osgan.reflections.common.Allocator;
import dev.httpmarco.polocloud.api.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public final class CloudEventCallPacket extends Packet {

    private Event event;

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void onRead(CodecBuffer codecBuffer) {
        event = Allocator.allocate((Class<Event>) Class.forName(codecBuffer.readString()));
        event.read(codecBuffer);

    }

    @Override
    public void onWrite(CodecBuffer codecBuffer) {
        codecBuffer.writeString(event.getClass().getName());
        event.write(codecBuffer);
    }
}
