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

package dev.httpmarco.polocloud.proxy.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.GroupProperties;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProxyCommand implements SimpleCommand {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final String PREFIX = "<gradient:#00fdee:#118bd1><bold>PoloCloud</bold></gradient> <dark_gray>» <gray>";

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            invocation.source().sendMessage(this.miniMessage.deserialize(this.PREFIX + "<red>You must be a Player to execute this command."));
            return;
        }

        var args = invocation.arguments();
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
    public List<String> suggest(Invocation invocation) {
        if (!(invocation.source() instanceof Player)) {
            return Collections.emptyList();
        }

        var args = invocation.arguments();
        if (args.length == 1) {
            return List.of("maintenance");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("maintenance")) {
            return Arrays.asList("true", "false");
        }

        return Collections.emptyList();
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        //return invocation.source().hasPermission("proxy.command");
        return true;
    }

    private void sendUsage(Player player) {
        player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "/proxy maintenance <true/false> <dark_gray>- <gray>Set the maintenance state"));
    }

    private void handelMaintenance(Player player, String[] args) {
        if (args.length != 2 || (!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false"))) {
            sendUsage(player);
            return;
        }

        var state = Boolean.parseBoolean(args[1]);
        var cloudPlayer = CloudAPI.instance().playerProvider().find(player.getUniqueId());
        var group = CloudAPI.instance().serviceProvider().service(cloudPlayer.currentProxyName()).group();
        group.properties().putRaw(GroupProperties.MAINTENANCE, state);
        group.update();

        player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "<red>Maintenance set to: <gray>" + group.properties().property(GroupProperties.MAINTENANCE)));
    }


}
