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

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.services.CloudService;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CloudCommand implements SimpleCommand {

    private final String PREFIX = "§bPoloCloud §8» §7";

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            invocation.source().sendMessage(Component.text(this.PREFIX + "§cYou must be a Player to execute this command."));
            return;
        }

        if (!(player.hasPermission("cloud.command"))) {
            player.sendMessage(Component.text(this.PREFIX + "§cYou don't have the Permission for that!"));
            return;
        }

        final String[] args = invocation.arguments();

        if (args.length == 0) {
            this.sendUsage(player);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "group", "groups" -> {
                this.handleGroupCommand(player, args);
                return;
            }
            case "service", "ser", "serv" -> {
                this.handleServiceCommand(player, args);
                return;
            }
            default -> {
                this.sendUsage(player);
                return;
            }
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            return Collections.emptyList();
        }

        if (!(player.hasPermission("cloud.command"))) {
            return Collections.emptyList();
        }

        final String[] args = invocation.arguments();

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
            return Arrays.asList("shutdown");
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

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("cloud.command");
    }

    private void sendUsage(Player player) {
        player.sendMessage(Component.text(this.PREFIX + "§7/cloud group list §8- §7list all groups"));
        player.sendMessage(Component.text(this.PREFIX + "§7/cloud group <groupName> §8- §7show info about group"));
        player.sendMessage(Component.text(this.PREFIX + "§7/cloud group shutdown <groupName> §8- §7shutdown all server by a group"));

        player.sendMessage(Component.text(this.PREFIX + "§7/cloud service list §8- §7list all services"));
        player.sendMessage(Component.text(this.PREFIX + "§7/cloud service <serviceName> §8- §7show info about service"));
        player.sendMessage(Component.text(this.PREFIX + "§7/cloud service <serviceName> shutdown  §8- §7shutdown a service"));
        // todo: Adding copy Command for Copy the Server into the Template!
    }

    private void handleGroupCommand(Player player, String[] args) {
        switch (args.length) {
            case 2 -> {
                if (args[1].equalsIgnoreCase("list")) {
                    final List<CloudGroup> groups = CloudAPI.instance().groupProvider().groups();
                    player.sendMessage(Component.text(this.PREFIX + "§7Following §b" + groups.size() + " §7groups are loading:"));
                    groups.forEach(cloudGroup -> {
                        player.sendMessage(Component.text("§8- §f" + cloudGroup.name() + "§8: (§b" + cloudGroup + "§8)"));
                    });
                    return;
                }

                final String groupName = args[0];
                if (!CloudAPI.instance().groupProvider().isGroup(groupName)) {
                    player.sendMessage(Component.text(this.PREFIX + "§cThis group does not exists!"));
                    return;
                }

                final CloudGroup group = CloudAPI.instance().groupProvider().group(groupName);

                player.sendMessage(Component.text(this.PREFIX + "Name§8: §b" + groupName));
                player.sendMessage(Component.text(this.PREFIX + "Platform§8: §b" + group.platform().version()));
                player.sendMessage(Component.text(this.PREFIX + "Memory§8: §b" + group.memory()));
                player.sendMessage(Component.text(this.PREFIX + "Minimum online services§8: §b" + group.minOnlineServices()));
                if (group.properties() != null) {
                    player.sendMessage(Component.text(this.PREFIX + "Properties §8(§b" + group.properties().properties().size() + "§8): §b"));
                    group.properties().properties().forEach((groupProperties, o) -> {
                        player.sendMessage(Component.text("   §8- §b" + groupProperties.id() + " §8= §b" + o.toString()));
                    });
                }
                return;
            }
            case 3 -> {
                if (!(args[1].equalsIgnoreCase("shutdown"))) {
                    this.sendUsage(player);
                    return;
                }
                final String groupName = args[2];
                if (!CloudAPI.instance().groupProvider().isGroup(groupName)) {
                    player.sendMessage(Component.text(this.PREFIX + "§cThis group does not exists!"));
                    return;
                }
                final CloudGroup cloudGroup = CloudAPI.instance().groupProvider().group(groupName);
                CloudAPI.instance().serviceProvider().services(cloudGroup).forEach(CloudService::shutdown);
                player.sendMessage(Component.text(this.PREFIX + "You successfully stopped all services of group §b" + groupName + "§8!"));
                return;
            }
        }
    }

    private void handleServiceCommand(Player player, String[] args) {
        switch (args.length) {
            case 2 -> {
                if (args[1].equalsIgnoreCase("list")) {
                    final List<CloudService> services = CloudAPI.instance().serviceProvider().services();
                    player.sendMessage(Component.text(this.PREFIX + "Following §b" + services.size() + " §7services are loading§8:"));
                    services.forEach(cloudService -> {
                        player.sendMessage(Component.text("§8- §f" + cloudService.name() + "§8: (§b" + cloudService.group().name() + "§8)"));
                    });
                    return;
                }

                final String serviceName = args[1];
                final CloudService service = CloudAPI.instance().serviceProvider().service(serviceName);
                if (service == null) {
                    player.sendMessage(Component.text(this.PREFIX + "§cThis services does not exists!"));
                    return;
                }

                player.sendMessage(Component.text(this.PREFIX + "Name§8: §b" + serviceName));
                player.sendMessage(Component.text(this.PREFIX + "Platform§8: §b" + service.group().platform().version()));
                player.sendMessage(Component.text(this.PREFIX + "Current memory§8: §b-1"));
                player.sendMessage(Component.text(this.PREFIX + "Players§8: §b-1"));
                player.sendMessage(Component.text(this.PREFIX + "Maximal players§8: §b" + service.maxPlayers()));
                player.sendMessage(Component.text(this.PREFIX + "Port §8: §b" + service.port()));
                player.sendMessage(Component.text(this.PREFIX + "State§8: §b" + service.state()));

                if (service.properties() != null) {
                    player.sendMessage(Component.text(this.PREFIX + "Properties §8(§b" + service.properties().properties().size() + "§8): §b"));
                    service.properties().properties().forEach((groupProperties, o) -> {
                        player.sendMessage(Component.text("   §8- §b" + groupProperties.id() + " §8= §b" + o.toString()));
                    });
                }
                return;
            }
            case 3 -> {
                if (!(args[2].equalsIgnoreCase("shutdown"))) {
                    this.sendUsage(player);
                    return;
                }

                final String serviceName = args[1];
                var service = CloudAPI.instance().serviceProvider().service(serviceName);
                if (service == null) {
                    player.sendMessage(Component.text(this.PREFIX + "§cThis services does not exists!"));
                    return;
                }

                service.shutdown();
                player.sendMessage(Component.text(this.PREFIX + "§7This services §b" + service.name() + " §7stopping now.."));
                return;
            }
        }
    }

}
