package dev.httpmarco.polocloud.addons.proxy;

import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.api.utils.JsonPoint;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

@Getter
@Accessors(fluent = true)
public final class ProxyAddon {

    private static final Path CONFIGURATION_PATH = Path.of("plugins/polocloud-proxy/config.json");

    @Getter
    private static ProxyAddon instance;
    private static final Random RANDOM = new Random();

    private final ProxyConfig config;

    public ProxyAddon() {
        instance = this;

        this.config = loadConfiguration();
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


    @SneakyThrows
    public ProxyConfig loadConfiguration() {
        if (!Files.exists(CONFIGURATION_PATH)) {
            CONFIGURATION_PATH.getParent().toFile().mkdirs();

            var config = new ProxyConfig(new Motd[]{new Motd("Poo", "ist toll"), new Motd("Poo", "ist 2")}, new Motd[]{new Motd("polo", "mainteannce")}, "maintenance", new Tablist("a", "b"));

            Files.writeString(CONFIGURATION_PATH, JsonPoint.PRETTY_JSON.toJson(config));
            return config;
        }
        return JsonPoint.PRETTY_JSON.fromJson(Files.readString(CONFIGURATION_PATH), ProxyConfig.class);
    }
}
