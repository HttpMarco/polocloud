package dev.httpmarco.polocloud.plugin.bungeecord;

import dev.httpmarco.polocloud.plugin.PluginPlatformAction;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public final class BungeeCordPlatformAction implements PluginPlatformAction {

    @Override
    public void sendMessage(UUID uuid, String message) {
        player(uuid).sendMessage(new TextComponent(message));
    }

    @Override
    public void sendActionbar(UUID uuid, String message) {
        player(uuid).sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    @Override
    public void sendTitle(UUID uuid, String title, String subTitle, int fadeInt, int stay, int fadeOut) {
        player(uuid).sendTitle(ProxyServer.getInstance().createTitle().title(new TextComponent(title)).subTitle(new TextComponent(subTitle)).stay(stay).fadeIn(fadeInt).fadeOut(fadeOut));
    }

    @Override
    public void connect(UUID uuid, String serverId) {
        player(uuid).connect(ProxyServer.getInstance().getServerInfo(serverId));
    }

    public ProxiedPlayer player(UUID uuid) {
        return ProxyServer.getInstance().getPlayer(uuid);
    }
}
