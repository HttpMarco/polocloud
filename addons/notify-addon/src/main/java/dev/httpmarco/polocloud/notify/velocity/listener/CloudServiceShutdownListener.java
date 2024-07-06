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

package dev.httpmarco.polocloud.notify.velocity.listener;

import com.velocitypowered.api.proxy.ProxyServer;
import dev.httpmarco.polocloud.api.events.EventNode;
import dev.httpmarco.polocloud.api.events.service.CloudServiceShutdownEvent;
import dev.httpmarco.polocloud.notify.configuration.MessagesConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class CloudServiceShutdownListener {

    public CloudServiceShutdownListener(ProxyServer server, MessagesConfiguration messagesConfiguration, EventNode node) {
        node.addListener(CloudServiceShutdownEvent.class, event -> {

            server.getAllPlayers().stream().filter(player -> player.hasPermission("polocloud.addon.notify")).forEach(player ->
                    player.sendMessage(MiniMessage.miniMessage().deserialize(messagesConfiguration.serviceStopped().replace("%service%", event.cloudService().name()))));
        });
    }
}
