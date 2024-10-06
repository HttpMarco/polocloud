package dev.httpmarco.polocloud.plugin.velocity;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.properties.Property;
import dev.httpmarco.polocloud.api.services.ClusterService;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class VelocityCloudCommand implements SimpleCommand {

    private static final String PREFIX = "<gradient:#00fdee:#118bd1><bold>PoloCloud</bold></gradient> <dark_gray>» <gray>";

    @Override
    public void execute(Invocation invocation) {
        var args = invocation.arguments();

        if (args.length == 0) {
            sendUsage(invocation);
            return;
        }

        // handle group command
        if (args[0].equalsIgnoreCase("group")) {
            if (args.length == 1 || args.length == 2) {
                sendUsage(invocation);
                return;
            }

            if (args.length == 3) {
                var group = CloudAPI.instance().groupProvider().find(args[1]);
                if (group == null) {
                    sendMessage(invocation,"<gray>The group <aqua>" + args[1] + " <gray>could not be <red>found<gray>.");
                    return;
                }

                if (args[2].equalsIgnoreCase("info")) {
                    sendGroupInfo(invocation, group);
                    return;
                }

                if (args[2].equalsIgnoreCase("delete")) {
                    CloudAPI.instance().groupProvider().delete(group.name());
                    sendMessage(invocation,"The group <aqua>" + group.name() + "<gray> has been <red>deleted <gray>successfully.");
                    return;
                }

                if (args[2].equalsIgnoreCase("shutdown")) {
                    CompletableFuture.runAsync(() -> {
                        for (var service : group.services()) {
                            service.shutdown();
                        }
                    });
                    sendMessage(invocation,"The group <aqua>" + group.name() + "<gray> has been <yellow>stopped <gray>successfully.");
                    return;
                }

                sendUsage(invocation);
                return;
            }
            return;
        }

        // handle service command
        if (args[0].equalsIgnoreCase("service")) {
            if (args.length == 1 || args.length == 2) {
                sendUsage(invocation);
                return;
            }

            if (args.length == 3) {
                var service = CloudAPI.instance().serviceProvider().find(args[1]);
                if (service == null) {
                    sendMessage(invocation,"<gray>The service <aqua>" + args[1] + " <gray>could not be <red>found<gray>.");
                    return;
                }

                if (args[2].equalsIgnoreCase("info")) {
                    sendServiceInfo(invocation, service);
                    return;
                }

                if (args[2].equalsIgnoreCase("shutdown")) {
                    service.shutdown();
                    sendMessage(invocation,"The service <aqua>" + service.name() + "<gray> has been <yellow>stopped <gray>successfully.");
                    return;
                }
            }

            if (args.length >= 4 && args[2].equalsIgnoreCase("execute")) {
                if (args.length == 4) {
                    sendUsage(invocation);
                    return;
                }

                var service = CloudAPI.instance().serviceProvider().find(args[1]);
                if (service == null) {
                    sendMessage(invocation,"<gray>The service <aqua>" + args[1] + " <gray>could not be <red>found<gray>.");
                    return;
                }

                var command = String.join(" ", Stream.of(args).skip(3).toList());
                service.executeCommand(command);

                var executorName = (invocation.source() instanceof Player) ? ((Player) invocation.source()).getUsername() : "Console";
                sendMessage(invocation,"<aqua>" + executorName + " <dark_gray>-> <aqua>" + service.name() + "<dark_gray>: <gray>" + command);
            }
        }
    }

    private void sendUsage(Invocation invocation) {
        sendMessage(invocation, "<white><st>    </st> Cloud Command Usage <st>    </st>");
        sendMessage(invocation, "<gray>/cloud group <name> info");
        sendMessage(invocation, "<gray>/cloud group <name> delete");
        sendMessage(invocation, "<gray>/cloud group <name> shutdown");
        sendMessage(invocation, "<gray>/cloud group <name> edit <property> <value>");
        sendMessage(invocation, "<gray>/cloud service <name> info");
        sendMessage(invocation, "<gray>/cloud service <name> shutdown");
        sendMessage(invocation, "<gray>/cloud service <name> execute <command>");
    }

    private void sendGroupInfo(Invocation invocation, ClusterGroup group) {
        sendMessage(invocation, "<white><st>    </st> Cluster Group Information <st>    </st>");
        sendMessage(invocation, "<gray>Group Name: <aqua>" + group.name());
        sendMessage(invocation, "<gray>Nodes: <aqua>" + String.join(", ", group.nodes()));
        sendMessage(invocation, "<gray>Templates: <aqua>" + String.join(", ", group.templates()));
        sendMessage(invocation, "<gray>Max Memory: <aqua>" + group.maxMemory() + " MB");
        sendMessage(invocation, "<gray>Max Players: <aqua>" + group.maxPlayers());
        sendMessage(invocation, "<gray>Static Service: <aqua>" + (group.staticService() ? "Yes" : "No"));
        sendMessage(invocation, "<gray>Platform: <aqua>" + group.platform().details());
        sendMessage(invocation, "<gray>Min Online Instances: <aqua>" + group.minOnlineServerInstances());
        sendMessage(invocation, "<gray>Max Online Instances: <aqua>" + group.maxOnlineServerInstances());
        sendMessage(invocation, "<gray>Service Count: <aqua>" + group.serviceCount());
    }

    private void sendServiceInfo(Invocation invocation, ClusterService service) {
        sendMessage(invocation, "<white><st>    </st> Cluster Service Information <st>    </st>");
        sendMessage(invocation, "<gray>Id: <aqua>" + service.id());
        sendMessage(invocation, "<gray>Ordered Id: <aqua>" + service.orderedId());
        sendMessage(invocation, "<gray>Service Name: <aqua>" + service.name());
        sendMessage(invocation, "<gray>Group: <aqua>" + service.group().name());
        sendMessage(invocation, "<gray>Hostname: <aqua>" + service.hostname());
        sendMessage(invocation, "<gray>Port: <aqua>" + service.port());
        sendMessage(invocation, "<gray>Running Node: <aqua>" + service.runningNode());
        sendMessage(invocation, "<gray>State: <aqua>" + service.state().name());
        sendMessage(invocation, "<gray>Max Players: <aqua>" + service.maxPlayers());
        sendMessage(invocation, "<gray>Online Players: <aqua>" + service.onlinePlayersCount());
    }

    private void sendMessage(Invocation invocation, String message) {
        invocation.source().sendMessage(MiniMessage.miniMessage().deserialize(PREFIX + message));
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("polocloud.plugin.cloud.command");
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        var args = invocation.arguments();

        if (args.length == 0) {
            return List.of("group", "service");
        }

        if (args.length == 1) {
            return Stream.of("group", "service")
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("group")) {
                return CloudAPI.instance().groupProvider().groups().stream()
                        .map(Named::name)
                        .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                        .toList();
            }

            if (args[0].equalsIgnoreCase("service")) {
                return CloudAPI.instance().serviceProvider().services().stream()
                        .map(Named::name)
                        .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                        .toList();
            }
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("group")) {
                return Stream.of("delete", "edit", "info", "shutdown")
                        .filter(action -> action.toLowerCase().startsWith(args[2].toLowerCase()))
                        .toList();
            }

            if (args[0].equalsIgnoreCase("service")) {
                return Stream.of("execute", "info", "shutdown")
                        .filter(action -> action.toLowerCase().startsWith(args[2].toLowerCase()))
                        .toList();
            }
        }

        if (args.length == 4 && args[0].equalsIgnoreCase("group") && args[2].equalsIgnoreCase("edit")) {
            var group = CloudAPI.instance().groupProvider().find(args[1]);

            if (group == null) {
                return List.of("");
            }

            return group.properties().pool().keySet().stream()
                    .map(Property::name)
                    .filter(property -> property.toLowerCase().startsWith(args[3].toLowerCase()))
                    .toList();
        }

        if (args.length == 5 && args[0].equalsIgnoreCase("group") && args[2].equalsIgnoreCase("edit")) {
            if (args[4].isBlank()) {
                return List.of("<value>");
            } else {
                return List.of();
            }
        }

        if (args.length == 4 && args[0].equalsIgnoreCase("service") && args[2].equalsIgnoreCase("execute")) {
            return Stream.of("<command>")
                    .filter(command -> command.toLowerCase().startsWith(args[3].toLowerCase()))
                    .toList();
        }

        return List.of();
    }
}
