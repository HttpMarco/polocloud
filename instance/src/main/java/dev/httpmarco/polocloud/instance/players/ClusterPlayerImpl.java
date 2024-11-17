package dev.httpmarco.polocloud.instance.players;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.packet.resources.player.PlayerConnectPacket;
import dev.httpmarco.polocloud.api.players.AbstractClusterPlayer;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.instance.ClusterInstance;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class ClusterPlayerImpl extends AbstractClusterPlayer {

    public ClusterPlayerImpl(String name, UUID uniqueId, String currentServerName, String currentProxyName) {
        super(name, uniqueId, currentServerName, currentProxyName);
    }

    @Override
    public CompletableFuture<ClusterService> currentProxyAsync() {
        return CloudAPI.instance().serviceProvider().findAsync(currentProxyName());
    }

    @Override
    public CompletableFuture<ClusterService> currentServerAsync() {
        return CloudAPI.instance().serviceProvider().findAsync(currentServerName());
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        // todo
    }

    @Override
    public void sendTablist(String header, String footer) {
        // todo
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        // todo
    }

    @Override
    public void sendMessage(String message) {
        // todo
    }

    @Override
    public void sendActionBar(String message) {
        // todo
    }

    @Override
    public void connect(String serviceId) {
        if(this.currentProxyName().equals(ClusterInstance.instance().selfService().name())) {
            ClusterInstance.instance().client().call(new PlayerConnectPacket(uniqueId(), serviceId), null);
            return;
        }
        ClusterInstance.instance().client().sendPacket(new PlayerConnectPacket(uniqueId(), serviceId));
    }
}
