package dev.httpmarco.polocloud.addons.proxy.bungeecord;

import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class BungeeCordProxyCommand extends Command {

    public BungeeCordProxyCommand() {
        super("proxy");
    }

    @Contract(pure = true)
    @Override
    public void execute(@NotNull CommandSender sender, String @NotNull [] args) {

        //todo
        if (sender.hasPermission("polocloud.addon.proxy.command")) {
            sender.sendMessage(new TextComponent("§cYou dont have permission for it!"));
            return;
        }

        if (args[0].equalsIgnoreCase("maintenance")) {

            if (args.length == 2) {
                var state = args[1];

                if (state.equalsIgnoreCase("on") || state.equalsIgnoreCase("false")) {
                    var service = ClusterInstance.instance().selfService();
                    service.group().properties().put(GroupProperties.MAINTENANCE, state.equalsIgnoreCase("on"));
                    service.group().update();

                    // todo kick all players without the permission
                    sender.sendMessage(new TextComponent("You update the maintenance mode!"));
                    return;
                }
            }
            sender.sendMessage(new TextComponent("Use following command: proxy maintenance <on/off>"));
            return;
        }
        // todo add more command (z.B. max players)
        sender.sendMessage(new TextComponent("Use following command: proxy maintenance <on/off>"));
    }
}
