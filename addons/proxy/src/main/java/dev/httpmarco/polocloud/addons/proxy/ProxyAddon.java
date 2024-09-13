package dev.httpmarco.polocloud.addons.proxy;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class ProxyAddon {

    @Getter
    private static ProxyAddon instance;

    public ProxyAddon() {
        instance = this;
    }

    public int getOnline() {
        return ProxyPingDelayedTicker.onlineCount();
    }

    public int getMaxPlayers() {
        // todo
        return -1;
    }

    public boolean hasMaxPlayers() {
        // todo
        return false;
    }
}
