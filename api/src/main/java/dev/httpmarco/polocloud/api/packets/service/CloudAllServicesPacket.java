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

package dev.httpmarco.polocloud.api.packets.service;

import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.polocloud.api.packets.CloudComponentPacketHelper;
import dev.httpmarco.polocloud.api.services.CloudService;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class CloudAllServicesPacket extends Packet {

    private List<CloudService> services;

    public CloudAllServicesPacket(List<CloudService> services) {
        this.services = services;
    }

    @Override
    public void onRead(CodecBuffer codecBuffer) {
        this.services = new ArrayList<>();

        int amount = codecBuffer.readInt();
        for (int i = 0; i < amount; i++) {
            this.services.add(CloudComponentPacketHelper.readService(codecBuffer));
        }
    }

    @Override
    public void onWrite(CodecBuffer codecBuffer) {
        codecBuffer.writeInt(services.size());

        for (CloudService service : services) {
            CloudComponentPacketHelper.writeService(service, codecBuffer);
        }
    }
}
