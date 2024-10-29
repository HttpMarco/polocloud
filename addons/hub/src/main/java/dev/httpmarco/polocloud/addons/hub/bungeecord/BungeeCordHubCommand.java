package dev.httpmarco.polocloud.addons.hub.bungeecord;

import dev.httpmarco.polocloud.addons.hub.HubAddon;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.services.ClusterServiceFilter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BungeeCordHubCommand extends Command {

    private static final String PREFIX = "§b§lPoloCloud-Hub §8» §7";

    public BungeeCordHubCommand(String name, String... aliases) {
        super(name, "polocloud.addon.hub.command", aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        var messages = HubAddon.instance().config().messages();

        if (!(commandSender instanceof ProxiedPlayer player)) {
            commandSender.sendMessage(new TextComponent(PREFIX + messages.noPlayer()));
            return;
        }

        var cloudPlayer = CloudAPI.instance().playerProvider().find(player.getUniqueId());
        if (cloudPlayer == null) {
            player.sendMessage(new TextComponent(PREFIX + messages.errorOccurred()));
            return;
        }

        var playerCurrentServer = cloudPlayer.currentServer();
        if (playerCurrentServer == null) {
            player.sendMessage(new TextComponent(PREFIX + messages.errorOccurred()));
            return;
        }

        var fallbackCheck = CloudAPI.instance().serviceProvider().findAsync(ClusterServiceFilter.FALLBACKS);
        if (fallbackCheck.join().isEmpty()) {
            player.sendMessage(new TextComponent(PREFIX + messages.noFallbackFound()));
            return;
        }

        if (fallbackCheck.join().stream().filter(service -> service.id().equals(cloudPlayer.currentServer().id())).findAny().orElse(null) != null) {
            player.sendMessage(new TextComponent(PREFIX + messages.alreadyConnected()));
            return;
        }

        var lowestFallbackService = CloudAPI.instance().serviceProvider().findAsync(ClusterServiceFilter.LOWEST_FALLBACK);
        if (lowestFallbackService.join().isEmpty()) {
            player.sendMessage(new TextComponent(PREFIX + messages.noFallbackFound()));
            return;
        }

        var targetService = lowestFallbackService.join().get(0);

        cloudPlayer.connect(targetService);
        player.sendMessage(new TextComponent(PREFIX + messages.successfullyConnected().formatted(targetService.name())));
    }
}
