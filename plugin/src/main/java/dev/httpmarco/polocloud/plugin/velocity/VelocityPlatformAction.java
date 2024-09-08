package dev.httpmarco.polocloud.plugin.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.httpmarco.polocloud.plugin.PluginPlatformAction;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.kyori.adventure.util.Ticks;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@AllArgsConstructor
public final class VelocityPlatformAction implements PluginPlatformAction {

    private ProxyServer proxyServer;

    @Override
    public void sendMessage(UUID uuid, String message) {
        player(uuid).sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    @Override
    public void sendActionbar(UUID uuid, String message) {
        player(uuid).sendActionBar(MiniMessage.miniMessage().deserialize(message));
    }

    @Override
    public void sendTitle(UUID uuid, String title, String subTitle, int fadeInt, int stay, int fadeOut) {
        var player = player(uuid);

        player.sendTitlePart(TitlePart.TITLE, MiniMessage.miniMessage().deserialize(title));
        player.sendTitlePart(TitlePart.SUBTITLE, MiniMessage.miniMessage().deserialize(subTitle));
        player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Ticks.duration(fadeInt), Ticks.duration(stay), Ticks.duration(fadeOut)));
    }

    @Override
    public void connect(UUID uuid, String serverId) {
        proxyServer.getServer(serverId).ifPresent(it -> player(uuid).createConnectionRequest(it).fireAndForget());
    }

    private @NotNull Player player(UUID uuid) {
        return proxyServer.getPlayer(uuid).get();
    }
}
