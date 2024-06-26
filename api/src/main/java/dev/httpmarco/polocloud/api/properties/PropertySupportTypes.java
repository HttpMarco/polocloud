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

package dev.httpmarco.polocloud.api.properties;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.function.BiConsumer;
import java.util.function.Function;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public enum PropertySupportTypes {

    STRING((buffer, it) -> buffer.writeString((String) it), PacketBuffer::readString),
    INTEGER((buffer, it) -> buffer.writeInt((Integer) it), PacketBuffer::readInt),
    BOOLEAN((buffer, it) -> buffer.writeBoolean((Boolean) it), PacketBuffer::readBoolean),
    LONG((buffer, it) -> buffer.writeLong((Long) it), PacketBuffer::readLong);

    //todo
    // uuid
    //float
    // short
    // double
    // enum

    private final BiConsumer<PacketBuffer, Object> writer;
    private final Function<PacketBuffer, Object> reader;

}
