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

package dev.httpmarco.polocloud.base.node;

import dev.httpmarco.osgan.files.annotations.ConfigExclude;
import dev.httpmarco.osgan.networking.server.CommunicationServer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.node.AbstractNode;
import dev.httpmarco.polocloud.api.packets.service.CloudServiceRegisterPacket;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class LocalNode extends AbstractNode implements dev.httpmarco.polocloud.api.node.LocalNode {

    @ConfigExclude
    private CommunicationServer server;

    public LocalNode(UUID id, String name, String hostname, int port) {
        super(id, name, hostname, port);
    }

    public void initialize() {
        server = new CommunicationServer(hostname(), port());
        server.initialize();

        server.listen(CloudServiceRegisterPacket.class, (channelTransmit, cloudServiceRegisterPacket) -> {
            var service = CloudAPI.instance().serviceProvider().find(cloudServiceRegisterPacket.uuid());

            if (service instanceof LocalCloudService localCloudService) {
                localCloudService.channelTransmit(channelTransmit);
            } else {
                //todo
            }
        });
    }

    @Override
    public void close() {
        if (this.server != null) {
            this.server.close();
        }
    }
}
