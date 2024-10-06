package dev.httpmarco.polocloud.addons.proxy.velocity;

import com.velocitypowered.api.command.SimpleCommand;
import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;

public final class VelocityProxyCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        var args = invocation.arguments();

        if (args[0].equalsIgnoreCase("maintenance")) {

            if (args.length == 2) {
                var state = args[1];

                if (state.equalsIgnoreCase("on") || state.equalsIgnoreCase("false")) {
                    var service = ClusterInstance.instance().selfService();
                    service.group().properties().put(GroupProperties.MAINTENANCE, state.equalsIgnoreCase("on"));
                    service.group().update();

                    // todo kick all players without the permission
                    invocation.source().sendMessage(MiniMessage.miniMessage().deserialize("You update the maintenance mode!"));
                    return;
                }
            }
            invocation.source().sendMessage(MiniMessage.miniMessage().deserialize("Use following command: proxy maintenance <on/off>"));
            return;
        }
        // todo add more command (z.B. max players)
        invocation.source().sendMessage(MiniMessage.miniMessage().deserialize("Use following command: proxy maintenance <on/off>"));
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("polocloud.addon.proxy.command");
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        var args = invocation.arguments();

        if (args.length == 0) {
            return List.of("maintenance");
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("maintenance")) {
            return List.of("on", "off");
        }
        /*
        if (args.length == 2 && args[0].equalsIgnoreCase("maintenance")) {
            return List.of();
        }*/

        return List.of();
    }
}
