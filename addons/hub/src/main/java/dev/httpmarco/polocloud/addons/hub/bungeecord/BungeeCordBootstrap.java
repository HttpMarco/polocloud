package dev.httpmarco.polocloud.addons.hub.bungeecord;

import dev.httpmarco.polocloud.addons.hub.HubAddon;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Arrays;

public class BungeeCordBootstrap extends Plugin {

    @Override
    public void onEnable() {

        new HubAddon(false);

        var hubCommands = HubAddon.instance().config().hubCommands();
        var name = hubCommands.length == 0 ? "hub" : hubCommands[0];

        String[] aliases = null;
        if (hubCommands.length > 1) {
            aliases = Arrays.copyOfRange(hubCommands, 1, hubCommands.length);
        }
        getProxy().getPluginManager().registerCommand(this, new BungeeCordHubCommand(name, aliases));
    }
}
