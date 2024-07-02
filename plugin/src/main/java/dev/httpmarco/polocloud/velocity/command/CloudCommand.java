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

package dev.httpmarco.polocloud.velocity.command;

import com.google.common.collect.Lists;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.services.CloudService;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CloudCommand implements SimpleCommand {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final String PREFIX = "<aqua>PoloCloud <dark_gray>» <gray>";

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
            case "group", "groups" -> this.handleGroupCommand(player, args);
            case "service", "ser", "serv" -> this.handleServiceCommand(player, args);
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
            return Arrays.asList("group", "service");
        }

        if (args.length == 2 && args[0].toLowerCase().startsWith("ser")) {
            List<String> list = Lists.newArrayList();
            list.add("list");
            CloudAPI.instance().serviceProvider().services().forEach(cloudService -> list.add(cloudService.name()));
            return list;
        }

        if (args.length == 3 && args[0].toLowerCase().startsWith("ser")) {
            return List.of("shutdown");
        }

        if (args.length == 2 && args[0].toLowerCase().startsWith("group")) {
            List<String> list = Lists.newArrayList();
            list.add("list");
            CloudAPI.instance().groupProvider().groups().forEach(cloudGroup -> list.add(cloudGroup.name()));
            return list;
        }

        if (args.length == 3 && args[0].toLowerCase().startsWith("group")) {
            return List.of("shutdown");
        }
        return Collections.emptyList();
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("cloud.command");
    }

    private void sendUsage(Player player) {
        player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "/cloud group list <dark_gray>- <gray>List all groups"));
        player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "/cloud group <groupName> <dark_gray>- <gray>Show info about a group"));
        player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "/cloud group <groupName> shutdown <dark_gray>- <gray>Shutdown all services by a group"));

        player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "/cloud service list <dark_gray>- <gray>List all services"));
        player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "/cloud service <serviceName> <dark_gray>- <gray>Show info about a service"));
        player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "/cloud service <serviceName> shutdown <dark_gray>- <gray>Shutdown a service"));
        // todo: Adding copy Command for Copy the Server into the Template!
    }

    private void handleGroupCommand(Player player, String[] args) {
        switch (args.length) {
            case 2 -> {
                if (args[1].equalsIgnoreCase("list")) {
                    var groups = CloudAPI.instance().groupProvider().groups();
                    player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "Following <aqua>" + groups.size() + " <gray>groups are loaded<dark_gray>:"));
                    groups.forEach(cloudGroup -> player.sendMessage(this.miniMessage.deserialize("<gray>- <white>" + cloudGroup.name())));
                    return;
                }

                var groupName = args[1];
                if (!CloudAPI.instance().groupProvider().isGroup(groupName)) {
                    player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "<red>This group does not exists!"));
                    return;
                }

                var group = CloudAPI.instance().groupProvider().group(groupName);

                player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "Name<dark_gray>: <aqua>" + groupName));
                player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "Platform<dark_gray>: <aqua>" + group.platform().version()));
                player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "Memory<dark_gray>: <aqua>" + group.memory()));
                player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "Minimum online services<dark_gray>: <aqua>" + group.minOnlineService()));

                if (group.properties() != null) {
                    player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "Properties <dark_gray>(<aqua>" +
                            group.properties().pool().size() + "<dark_gray>):"));
                    group.properties().pool().forEach((groupProperties, o) ->
                            player.sendMessage(this.miniMessage.deserialize("<dark_gray>   - <aqua>" + groupProperties +
                                    " <dark_gray>= <aqua>" + o))
                    );
                }
            }
            case 3 -> {
                if (!(args[2].equalsIgnoreCase("shutdown"))) {
                    this.sendUsage(player);
                    return;
                }
                var groupName = args[1];
                if (!CloudAPI.instance().groupProvider().isGroup(groupName)) {
                    player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "<red>This group does not exists!"));
                    return;
                }
                var cloudGroup = CloudAPI.instance().groupProvider().group(groupName);
                CloudAPI.instance().serviceProvider().services(cloudGroup).forEach(CloudService::shutdown);
                player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "<gray>You successfully stopped all services of group <aqua>" +
                        groupName + "<dark_gray>!"));
            }
        }
    }

    private void handleServiceCommand(Player player, String[] args) {
        switch (args.length) {
            case 2 -> {
                if (args[1].equalsIgnoreCase("list")) {
                    var services = CloudAPI.instance().serviceProvider().services();
                    player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "Following <aqua>" + services.size() +
                            " <gray>services are loaded<dark_gray>:"));
                    services.forEach(cloudService -> player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "- <white>" +
                            cloudService.name() + "<dark_gray>: (<aqua>" + cloudService.group().name() + "<dark_gray>)")));
                    return;
                }

                var serviceName = args[1];
                var service = CloudAPI.instance().serviceProvider().service(serviceName);
                if (service == null) {
                    player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "<red>This service does not exists!"));
                    return;
                }

                player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "Name<dark_gray>: <aqua>" + serviceName));
                player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "Platform<dark_gray>: <aqua>" + service.group().platform().version()));
                player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "Current memory<dark_gray>: <aqua>" + service.currentMemory()));
                player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "Players<dark_gray>: <aqua>" + service.onlinePlayersCount()));
                player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "Maximal players<dark_gray>: <aqua>" + service.maxPlayers()));
                player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "Port <dark_gray>: <aqua>" + service.port()));
                player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "State<dark_gray>: <aqua>" + service.state()));

                if (service.properties() != null) {
                    player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "Properties <dark_gray>(<aqua>" +
                            service.properties().pool().size() + "<dark_gray>):"));
                    service.properties().pool().forEach((groupProperties, o) ->
                            player.sendMessage(this.miniMessage.deserialize("<dark_gray>   - <aqua>" + groupProperties +
                                    " <dark_gray>= <aqua>" + o))
                    );
                }
            }
            case 3 -> {
                if (!(args[2].equalsIgnoreCase("shutdown"))) {
                    this.sendUsage(player);
                    return;
                }

                var serviceName = args[1];
                var service = CloudAPI.instance().serviceProvider().service(serviceName);
                if (service == null) {
                    player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "<red>This service does not exists!"));
                    return;
                }

                service.shutdown();
                player.sendMessage(this.miniMessage.deserialize(this.PREFIX + "The service <aqua>" + serviceName + " <gray>is stopping now..."));
            }
        }
    }

}
