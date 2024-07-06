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

package dev.httpmarco.polocloud.hubcommand.velocity.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.api.services.ServiceFilter;
import dev.httpmarco.polocloud.hubcommand.configuration.MessagesConfiguration;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;

@RequiredArgsConstructor
public class HubCommand implements SimpleCommand {

    private final ProxyServer server;
    private final MessagesConfiguration messagesConfiguration;

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            return;
        }

        final var cloudPlayer = CloudAPI.instance().playerProvider().find(player.getUniqueId());
        final var currentService = CloudAPI.instance().serviceProvider().find(cloudPlayer.currentServerName());

        if (currentService.group().properties().has(GroupProperties.FALLBACK)) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(messagesConfiguration.prefix() + messagesConfiguration.alreadyOnFallback()));
            return;
        }

        var service = CloudAPI.instance().serviceProvider().filterService(ServiceFilter.LOWEST_FALLBACK);
        if (service.isEmpty()) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(messagesConfiguration.prefix() + messagesConfiguration.noFallbackFound()));
            return;
        }

        var server = this.server.getServer(service.get(0).name());
        if (server.isEmpty()) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(messagesConfiguration.prefix() + messagesConfiguration.noFallbackFound()));
            return;
        }

        player.createConnectionRequest(server.get()).fireAndForget();
        player.sendMessage(MiniMessage.miniMessage().deserialize(messagesConfiguration.prefix() + messagesConfiguration.successfullyConnected().replace("%server%", server.get().getServerInfo().getName())));
    }
}
