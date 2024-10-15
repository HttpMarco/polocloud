package dev.httpmarco.polocloud.plugin.bungeecord;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.properties.Property;
import dev.httpmarco.polocloud.api.services.ClusterService;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class BungeeCordCloudCommand extends Command implements TabExecutor {

    private static final String PREFIX = "§b§lPoloCloud §r§8» §7";

    public BungeeCordCloudCommand() {
        super("cloud", null);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return;
        }

        // handle group command
        if (args[0].equalsIgnoreCase("group")) {
            if (args.length == 1 || args.length == 2) {
                sendUsage(sender);
                return;
            }

            if (args.length == 3) {
                var group = CloudAPI.instance().groupProvider().find(args[1]);
                if (group == null) {
                    sendMessage(sender,"§7The group §b" + args[1] + " §7could not be §cfound§7.");
                    return;
                }

                if (args[2].equalsIgnoreCase("info")) {
                    sendGroupInfo(sender, group);
                    return;
                }

                if (args[2].equalsIgnoreCase("delete")) {
                    CloudAPI.instance().groupProvider().delete(group.name());
                    sendMessage(sender,"The group §b" + group.name() + "§7 has been §cdeleted §7successfully.");
                    return;
                }

                if (args[2].equalsIgnoreCase("shutdown")) {
                    CompletableFuture.runAsync(() -> {
                        for (var service : group.services()) {
                            service.shutdown();
                        }
                    });
                    sendMessage(sender,"The group §b" + group.name() + "§7 has been §estopped §7successfully.");
                    return;
                }

                sendUsage(sender);
                return;
            }
            return;
        }

        // handle service command
        if (args[0].equalsIgnoreCase("service")) {
            if (args.length == 1 || args.length == 2) {
                sendUsage(sender);
                return;
            }

            if (args.length == 3) {
                var service = CloudAPI.instance().serviceProvider().find(args[1]);
                if (service == null) {
                    sendMessage(sender,"§7The service §b" + args[1] + " §7could not be §cfound§7.");
                    return;
                }

                if (args[2].equalsIgnoreCase("info")) {
                    sendServiceInfo(sender, service);
                    return;
                }

                if (args[2].equalsIgnoreCase("shutdown")) {
                    service.shutdown();
                    sendMessage(sender,"The service §b" + service.name() + "§7 has been §estopped §7successfully.");
                    return;
                }
            }

            if (args.length >= 4 && args[2].equalsIgnoreCase("execute")) {
                if (args.length == 4) {
                    sendUsage(sender);
                    return;
                }

                var service = CloudAPI.instance().serviceProvider().find(args[1]);
                if (service == null) {
                    sendMessage(sender,"§7The service §b" + args[1] + " §7could not be §cfound§7.");
                    return;
                }

                var command = String.join(" ", Stream.of(args).skip(3).toList());
                service.executeCommand(command);

                sendMessage(sender,"§b" + sender.getName() + " §8-> §b" + service.name() + "§8: §7" + command);
            }
        }
    }

    private void sendUsage(CommandSender sender) {
        sendMessage(sender, "§f§m    §r§f Cloud Command Usage §m    ");
        sendMessage(sender, "§7/cloud group <name> info");
        sendMessage(sender, "§7/cloud group <name> delete");
        sendMessage(sender, "§7/cloud group <name> shutdown");
        sendMessage(sender, "§7/cloud group <name> edit <property> <value>");
        sendMessage(sender, "§7/cloud service <name> info");
        sendMessage(sender, "§7/cloud service <name> shutdown");
        sendMessage(sender, "§7/cloud service <name> execute <command>");
    }

    private void sendGroupInfo(CommandSender sender, ClusterGroup group) {
        sendMessage(sender, "§f§m    §r§f Cluster Group Information §m    ");
        sendMessage(sender, "§7Group Name: §b" + group.name());
        sendMessage(sender, "§7Nodes: §b" + String.join(", ", group.nodes()));
        sendMessage(sender, "§7Templates: §b" + String.join(", ", group.templates()));
        sendMessage(sender, "§7Max Memory: §b" + group.maxMemory() + " MB");
        sendMessage(sender, "§7Max Players: §b" + group.maxPlayers());
        sendMessage(sender, "§7Static Service: §b" + (group.staticService() ? "Yes" : "No"));
        sendMessage(sender, "§7Platform: §b" + group.platform().details());
        sendMessage(sender, "§7Min Online Instances: §b" + group.minOnlineServerInstances());
        sendMessage(sender, "§7Max Online Instances: §b" + group.maxOnlineServerInstances());
        sendMessage(sender, "§7Service Count: §b" + group.serviceCount());
    }

    private void sendServiceInfo(CommandSender sender, ClusterService service) {
        sendMessage(sender, "§f§m    §r§f Cluster Service Information §m    ");
        sendMessage(sender, "§7Id: §b" + service.id());
        sendMessage(sender, "§7Ordered Id: §b" + service.orderedId());
        sendMessage(sender, "§7Service Name: §b" + service.name());
        sendMessage(sender, "§7Group: §b" + service.group().name());
        sendMessage(sender, "§7Hostname: §b" + service.hostname());
        sendMessage(sender, "§7Port: §b" + service.port());
        sendMessage(sender, "§7Running Node: §b" + service.runningNode());
        sendMessage(sender, "§7State: §b" + service.state().name());
        sendMessage(sender, "§7Max Players: §b" + service.maxPlayers());
        sendMessage(sender, "§7Online Players: §b" + service.onlinePlayersCount());
    }

    private void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(new TextComponent(PREFIX + message));
    }


    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
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
