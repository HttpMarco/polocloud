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

package dev.httpmarco.polocloud.hubcommand.bungeecord.command;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.api.services.ServiceFilter;
import dev.httpmarco.polocloud.hubcommand.configuration.MessagesConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.jetbrains.annotations.NotNull;

public class HubCommand extends Command {

    private final MessagesConfiguration messagesConfiguration;

    public HubCommand(MessagesConfiguration messagesConfiguration) {
        super("hub", null, "lobby", "leave", "l");
        this.messagesConfiguration = messagesConfiguration;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if ((!(sender instanceof ProxiedPlayer player))) {
            return;
        }

        final var cloudPlayer = CloudAPI.instance().playerProvider().find(player.getUniqueId());
        final var currentService = CloudAPI.instance().serviceProvider().find(cloudPlayer.currentServerName());

        if (currentService.group().properties().has(GroupProperties.FALLBACK)) {
            player.sendMessage(format(messagesConfiguration.prefix() + messagesConfiguration.alreadyOnFallback()));
            return;
        }

        var service = CloudAPI.instance().serviceProvider().filterService(ServiceFilter.LOWEST_FALLBACK);
        if (service.isEmpty()) {
            player.sendMessage(format(messagesConfiguration.prefix() + messagesConfiguration.noFallbackFound()));
            return;
        }

        var serverInfo = ProxyServer.getInstance().getServerInfo(service.get(0).name());
        if (serverInfo == null) {
            player.sendMessage(format(messagesConfiguration.prefix() + messagesConfiguration.noFallbackFound()));
            return;
        }

        player.connect(serverInfo);
        player.sendMessage(format(messagesConfiguration.prefix() + messagesConfiguration.successfullyConnected().replace("%server%", serverInfo.getName())));
    }

    private @NotNull BaseComponent @NotNull [] format(String text) {
        return BungeeComponentSerializer.get().serialize(MiniMessage.miniMessage().deserialize(text));
    }
}
