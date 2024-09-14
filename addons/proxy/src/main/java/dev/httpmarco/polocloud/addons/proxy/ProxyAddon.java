package dev.httpmarco.polocloud.addons.proxy;

import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Random;

@Getter
@Accessors(fluent = true)
public final class ProxyAddon {

    @Getter
    private static ProxyAddon instance;
    private static final Random RANDOM = new Random();

    private final ProxyConfig config;

    public ProxyAddon() {
        instance = this;

        this.config = new ProxyConfig(new Motd[]{new Motd("Poo", "ist toll"), new Motd("Poo", "ist 2")}, new Motd[]{new Motd("polo", "mainteannce")}, "maintenance", new Tablist("a", "b"));
    }

    public int getOnline() {
        return ProxyPingDelayedTicker.onlineCount();
    }

    public int getMaxPlayers() {
        return ClusterInstance.instance().selfService().maxPlayers();
    }

    public Motd motd() {
        return this.config.motds()[RANDOM.nextInt(config.motds().length)];
    }

    public Motd maintenanceMotd() {
        return this.config.maintenanceMotds()[RANDOM.nextInt(config.maintenanceMotds().length)];
    }

    public boolean maintenance() {
        var propertiesPool = ClusterInstance.instance().selfService().properties();
        return propertiesPool.has(GroupProperties.MAINTENANCE) && propertiesPool.property(GroupProperties.MAINTENANCE);
    }
}
