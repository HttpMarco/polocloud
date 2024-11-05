package dev.httpmarco.polocloud.addons.hub.velocity;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import dev.httpmarco.polocloud.addons.hub.HubAddon;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.services.ClusterServiceFilter;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class VelocityHubCommand implements SimpleCommand {

    private static final String PREFIX = "<gradient:#00fdee:#118bd1><bold>PoloCloud-Hub</bold></gradient> <dark_gray>» <gray>";

    @Override
    public void execute(Invocation invocation) {
        var messages = HubAddon.instance().config().messages();

        if (!(invocation.source() instanceof Player player)) {
            invocation.source().sendMessage(MiniMessage.miniMessage().deserialize(PREFIX + messages.noPlayer()));
            return;
        }

        var cloudPlayer = CloudAPI.instance().playerProvider().find(player.getUniqueId());
        if (cloudPlayer == null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(PREFIX + messages.errorOccurred()));
            return;
        }

        var playerCurrentServer = cloudPlayer.currentServer();
        if (playerCurrentServer == null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(PREFIX + messages.errorOccurred()));
            return;
        }

        var fallbackCheck = CloudAPI.instance().serviceProvider().findAsync(ClusterServiceFilter.FALLBACKS);
        if (fallbackCheck.join().isEmpty()) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(PREFIX + messages.noFallbackFound()));
            return;
        }

        if (fallbackCheck.join().stream().filter(service -> service.id().equals(cloudPlayer.currentServer().id())).findAny().orElse(null) != null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(PREFIX + messages.alreadyConnected()));
            return;
        }

        var lowestFallbackService = CloudAPI.instance().serviceProvider().findAsync(ClusterServiceFilter.EMPTIEST_FALLBACK);
        if (lowestFallbackService.join().isEmpty()) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(PREFIX + messages.noFallbackFound()));
            return;
        }

        var targetService = lowestFallbackService.join().get(0);

        cloudPlayer.connect(targetService);
        player.sendMessage(MiniMessage.miniMessage().deserialize(PREFIX + messages.successfullyConnected().formatted(targetService.name())));
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("polocloud.addon.hub.command");
    }
}
