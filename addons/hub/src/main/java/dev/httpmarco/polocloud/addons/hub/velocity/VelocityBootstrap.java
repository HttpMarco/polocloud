package dev.httpmarco.polocloud.addons.hub.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.httpmarco.polocloud.addons.hub.HubAddon;

import java.util.Arrays;

@Plugin(id = "polocloud-hub", name = "PoloCloud-Hub", version = "1.0.0-alpha-1", authors = {"RECHERGG"})
public final class VelocityBootstrap {

    private final ProxyServer server;

    @Inject
    public VelocityBootstrap(ProxyServer server) {
        this.server = server;
        new HubAddon(true);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        var commandManager = server.getCommandManager();
        var hubCommands = HubAddon.instance().config().hubCommands();
        var builder = commandManager.metaBuilder(hubCommands.length == 0 ? "hub" : hubCommands[0]);

        if (hubCommands.length > 1) {
            builder.aliases(Arrays.copyOfRange(hubCommands, 1, hubCommands.length));
        }

        commandManager.register(builder.plugin(this).build(), new VelocityHubCommand());
    }

}
