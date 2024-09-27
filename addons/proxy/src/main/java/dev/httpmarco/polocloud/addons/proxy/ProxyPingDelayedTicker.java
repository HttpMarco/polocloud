package dev.httpmarco.polocloud.addons.proxy;

import dev.httpmarco.polocloud.api.CloudAPI;
import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;

@UtilityClass
public class ProxyPingDelayedTicker {

    private int lastDetectedPlayers;
    private long lastPing = System.currentTimeMillis();

    public int onlineCount() {
        if (lastDetectedPlayers == -1 || System.currentTimeMillis() >= (lastPing + TimeUnit.SECONDS.toMillis(5))) {
            // new data
            lastDetectedPlayers = CloudAPI.instance().playerProvider().playersCount();
            lastPing = System.currentTimeMillis();
        }
        return lastDetectedPlayers;
    }
}