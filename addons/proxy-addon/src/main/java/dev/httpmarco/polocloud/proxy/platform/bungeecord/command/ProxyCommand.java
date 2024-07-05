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

package dev.httpmarco.polocloud.proxy.platform.bungeecord.command;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.GroupProperties;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProxyCommand extends Command implements TabExecutor {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final String PREFIX = "<gradient:#00fdee:#118bd1><bold>PoloCloud</bold></gradient> <dark_gray>» <gray>";

    public ProxyCommand() {
        super("proxy");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer player)) {
            sender.sendMessage(BungeeComponentSerializer.get().serialize(this.miniMessage.deserialize((this.PREFIX + "<red>You must be a Player to execute this command."))));
            return;
        }

        if (!(player.hasPermission("proxy.command"))) {
            player.sendMessage(BungeeComponentSerializer.get().serialize(this.miniMessage.deserialize((this.PREFIX + "<red>You don't have the Permission for that!"))));
            return;
        }

        if (args.length == 0) {
            this.sendUsage(player);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "maintenance" -> this.handelMaintenance(player, args);
            default -> this.sendUsage(player);
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer player)) {
            return Collections.emptyList();
        }
        if (!(player.hasPermission("proxy.command"))) {
            return Collections.emptyList();
        }

        if (args.length == 0 || args.length == 1) {
            return List.of("maintenance");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("maintenance")) {
            return Arrays.asList("true", "false");
        }

        return Collections.emptyList();
    }

    private void sendUsage(ProxiedPlayer player) {
        player.sendMessage(BungeeComponentSerializer.get().serialize(this.miniMessage.deserialize((this.PREFIX + "/proxy group list <dark_gray>- <gray>list all groups"))));
    }

    private void handelMaintenance(ProxiedPlayer player, String[] args) {
        if (args.length != 2 || (!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false"))) {
            sendUsage(player);
            return;
        }

        var state = Boolean.parseBoolean(args[1]);
        var cloudPlayer = CloudAPI.instance().playerProvider().find(player.getUniqueId());
        var group = CloudAPI.instance().serviceProvider().find(cloudPlayer.currentProxyName()).group();

        group.properties().put(GroupProperties.MAINTENANCE, state);
        group.update();

        player.sendMessage(BungeeComponentSerializer.get().serialize(this.miniMessage.deserialize(this.PREFIX + "<gray>You set the property <red>Maintenance <gray>with value <aqua>"+ group.properties().property(GroupProperties.MAINTENANCE) + " <gray>to group <aqua>" + group.name() + "<gray>.")));
    }
}
