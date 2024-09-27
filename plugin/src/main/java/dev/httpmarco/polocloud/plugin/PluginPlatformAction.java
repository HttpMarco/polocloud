package dev.httpmarco.polocloud.plugin;

import java.util.UUID;

public interface PluginPlatformAction {

    void sendMessage(UUID uuid, String message);

    void sendActionbar(UUID uuid, String message);

    void sendTitle(UUID uuid, String title, String subTitle, int fadeInt, int stay, int fadeOut);

    void connect(UUID uuid, String serverId);

    void sendTablist(UUID uuid, String header, String footer);

}
