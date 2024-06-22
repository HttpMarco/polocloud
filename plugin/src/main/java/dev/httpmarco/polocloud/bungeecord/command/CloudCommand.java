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

package dev.httpmarco.polocloud.bungeecord.command;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.services.CloudService;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CloudCommand extends Command implements TabExecutor {

    private final String PREFIX = "§bPoloCloud §8» §7";

    public CloudCommand() {
        super("cloud");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer player)) {
            sender.sendMessage(TextComponent.fromLegacy(this.PREFIX + "§cYou must be a Player to execute this command."));
            return;
        }

        if (!(player.hasPermission("cloud.command"))) {
            player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "§cYou don't have the Permission for that!"));
            return;
        }

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
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer player)) {
            return Collections.emptyList();
        }
        if (!(player.hasPermission("cloud.command"))) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            return Arrays.asList("group", "service");
        }

        if (args.length == 2 && args[0].toLowerCase().startsWith("service")) {
            List<String> list = new ArrayList<>();
            list.add("list");
            CloudAPI.instance().serviceProvider().services().forEach(cloudService -> {
                list.add(cloudService.name());
            });
            return list;
        }

        if (args.length == 3 && args[0].toLowerCase().startsWith("service")) {
            return List.of("shutdown");
        }

        if (args.length == 2 && args[0].toLowerCase().startsWith("group")) {
            List<String> list = new ArrayList<>();
            list.add("list");
            list.add("shutdown");
            CloudAPI.instance().groupProvider().groups().forEach(cloudGroup -> {
                list.add(cloudGroup.name());
            });
            return list;
        }

        if (args.length == 3 && args[0].toLowerCase().startsWith("group")) {
            List<String> list = new ArrayList<>();
            CloudAPI.instance().groupProvider().groups().forEach(cloudGroup -> {
                list.add(cloudGroup.name());
            });
            return list;
        }
        return Collections.emptyList();
    }

    private void sendUsage(ProxiedPlayer player) {
        player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "§7/cloud group list §8- §7list all groups"));
        player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "§7/cloud group <groupName> §8- §7show info about group"));
        player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "§7/cloud group shutdown <groupName> §8- §7shutdown all server by a group"));

        player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "§7/cloud service list §8- §7list all services"));
        player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "§7/cloud service <serviceName> §8- §7show info about service"));
        player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "§7/cloud service <serviceName> shutdown  §8- §7shutdown a service"));
        // todo: Adding copy Command for Copy the Server into the Template!
    }

    private void handleGroupCommand(ProxiedPlayer player, String[] args) {
        switch (args.length) {
            case 2 -> {
                if (args[1].equalsIgnoreCase("list")) {
                    final List<CloudGroup> groups = CloudAPI.instance().groupProvider().groups();
                    player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "§7Following §b" + groups.size() + " §7groups are loading:"));
                    groups.forEach(cloudGroup -> {
                        player.sendMessage(TextComponent.fromLegacy("§8- §f" + cloudGroup.name() + "§8: (§b" + cloudGroup + "§8)"));
                    });
                    return;
                }

                final String groupName = args[0];
                if (!CloudAPI.instance().groupProvider().isGroup(groupName)) {
                    player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "§cThis group does not exists!"));
                    return;
                }

                final CloudGroup group = CloudAPI.instance().groupProvider().group(groupName);

                player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "Name§8: §b" + groupName));
                player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "Platform§8: §b" + group.platform().version()));
                player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "Memory§8: §b" + group.memory()));
                player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "Minimum online services§8: §b" + group.minOnlineService()));
                if (group.properties() != null) {
                    player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "Properties §8(§b" + group.properties().properties().size() + "§8): §b"));
                    group.properties().properties().forEach((groupProperties, o) -> player.sendMessage(TextComponent.fromLegacy("   §8- §b" + groupProperties.id() + " §8= §b" + o.toString())));
                }
            }
            case 3 -> {
                if (!(args[1].equalsIgnoreCase("shutdown"))) {
                    this.sendUsage(player);
                    return;
                }
                final String groupName = args[2];
                if (!CloudAPI.instance().groupProvider().isGroup(groupName)) {
                    player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "§cThis group does not exists!"));
                    return;
                }
                final CloudGroup cloudGroup = CloudAPI.instance().groupProvider().group(groupName);
                CloudAPI.instance().serviceProvider().services(cloudGroup).forEach(CloudService::shutdown);
                player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "You successfully stopped all services of group §b" + groupName + "§8!"));
            }
        }
    }

    private void handleServiceCommand(ProxiedPlayer player, String[] args) {
        switch (args.length) {
            case 2 -> {
                if (args[1].equalsIgnoreCase("list")) {
                    final List<CloudService> services = CloudAPI.instance().serviceProvider().services();
                    player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "Following §b" + services.size() + " §7services are loading§8:"));
                    services.forEach(cloudService -> {
                        player.sendMessage(TextComponent.fromLegacy("§8- §f" + cloudService.name() + "§8: (§b" + cloudService.group().name() + "§8)"));
                    });
                    return;
                }

                final String serviceName = args[1];
                final CloudService service = CloudAPI.instance().serviceProvider().service(serviceName);
                if (service == null) {
                    player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "§cThis services does not exists!"));
                    return;
                }

                player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "Name§8: §b" + serviceName));
                player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "Platform§8: §b" + service.group().platform().version()));
                player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "Current memory§8: §b-1"));
                player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "Players§8: §b-1"));
                player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "Maximal players§8: §b" + service.maxPlayers()));
                player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "Port §8: §b" + service.port()));
                player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "State§8: §b" + service.state()));

                if (service.properties() != null) {
                    player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "Properties §8(§b" + service.properties().properties().size() + "§8): §b"));
                    service.properties().properties().forEach((groupProperties, o) -> {
                        player.sendMessage(TextComponent.fromLegacy("   §8- §b" + groupProperties.id() + " §8= §b" + o.toString()));
                    });
                }
            }
            case 3 -> {
                if (!(args[2].equalsIgnoreCase("shutdown"))) {
                    this.sendUsage(player);
                    return;
                }

                final String serviceName = args[1];
                var service = CloudAPI.instance().serviceProvider().service(serviceName);
                if (service == null) {
                    player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "§cThis services does not exists!"));
                    return;
                }

                service.shutdown();
                player.sendMessage(TextComponent.fromLegacy(this.PREFIX + "§7This services §b" + service.name() + " §7stopping now.."));
            }
        }
    }
}
