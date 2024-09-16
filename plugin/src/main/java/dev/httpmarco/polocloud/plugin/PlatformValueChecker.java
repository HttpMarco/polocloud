package dev.httpmarco.polocloud.plugin;

import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class PlatformValueChecker {

    public <T> boolean reachMaxPlayers(@NotNull ProxyPluginPlatform<T> platform, T player) {
        return reachMaxPlayers(platform.parameterAdapter().onlinePlayers(), self().maxPlayers(), platform, player);
    }

    public <T> boolean reachMaxPlayers(int onlinePlayers, int maxPlayers, @NotNull ProxyPluginPlatform<T> platform, T player) {
        return onlinePlayers >= maxPlayers && !platform.parameterAdapter().hasPermission(player, PluginPermissions.BYPASS_MAX_PLAYERS);
    }


    public <T> boolean maintenanceEnabled(@NotNull ClusterService service, ProxyPluginPlatform<T> platform, T player) {
        // todo check group
        return service.properties().has(GroupProperties.MAINTENANCE) && service.properties().property(GroupProperties.MAINTENANCE) && !platform.parameterAdapter().hasPermission(player, PluginPermissions.BYPASS_MAINTENANCE);
    }

    public <T> boolean maintenanceEnabled(@NotNull ProxyPluginPlatform<T> platform, T player) {
        // todo check group
        return maintenanceEnabled(self(), platform, player);
    }


    private ClusterService self() {
        return ClusterInstance.instance().selfService();
    }
}
