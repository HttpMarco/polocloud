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

package dev.httpmarco.polocloud.runner;

import dev.httpmarco.osgan.networking.client.CommunicationClient;
import dev.httpmarco.polocloud.api.packets.service.CloudServiceRegisterPacket;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class CloudInstanceClient {

    private final CommunicationClient transmitter;

    @SneakyThrows
    public CloudInstanceClient(String hostname, int port, Runnable startup) {
        this.transmitter = new CommunicationClient(hostname, port, channelTransmit -> {
            channelTransmit.sendPacket(new CloudServiceRegisterPacket(CloudInstance.SELF_ID));
            startup.run();
        });
        this.transmitter.initialize();
    }
}
