package dev.httpmarco.polocloud.plugin;

// t instance of the player
public interface ProxyPlatformParameterAdapter<T> {

    boolean hasPermission(T player, String permission);

    int onlinePlayers();

}
