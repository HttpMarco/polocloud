package dev.httpmarco.polocloud.plugin;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.event.impl.services.ServiceOnlineEvent;
import dev.httpmarco.polocloud.api.event.impl.services.ServiceStoppingEvent;
import dev.httpmarco.polocloud.api.packet.resources.player.*;
import dev.httpmarco.polocloud.api.platforms.PlatformType;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceFilter;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class ProxyPluginPlatform<T> extends PluginPlatform {

    private final ProxyPlatformParameterAdapter<T> parameterAdapter;

    public ProxyPluginPlatform(PluginPlatformAction platformAction, @NotNull ProxyServerHandler proxyServerHandler, ProxyPlatformParameterAdapter<T> parameterAdapter) {
        this.parameterAdapter = parameterAdapter;
        var client = ClusterInstance.instance().client();

        // listen on players messages
        client.listen(PlayerMessagePacket.class, (transmit, packet) -> platformAction.sendMessage(packet.uuid(), packet.message()));

        // listen on actionbar
        client.listen(PlayerActionBarPacket.class, (transmit, packet) -> platformAction.sendActionbar(packet.uuid(), packet.message()));

        //
        client.listen(PlayerConnectPacket.class, (transmit, packet) -> platformAction.connect(packet.uuid(), packet.serverId()));

        // listen on player titles
        client.listen(PlayerTitlePacket.class, (transmit, packet) -> platformAction.sendTitle(packet.uuid(), packet.title(), packet.subTitle(), packet.fadeIn(), packet.stay(), packet.fadeOut()));

        var eventProvider = CloudAPI.instance().eventProvider();

        eventProvider.listen(ServiceOnlineEvent.class, event -> {
            if (event.service().group().platform().type() == PlatformType.SERVER) {
                proxyServerHandler.registerServer(event.service().name(), overwriteHostname(event.service()), event.service().port());
            }
        });

        eventProvider.listen(ServiceStoppingEvent.class, event -> {
            proxyServerHandler.unregisterServer(event.service().name());
        });


        for (var registered : proxyServerHandler.services()) {
            proxyServerHandler.unregisterServer(registered);
        }

        for (var service : CloudAPI.instance().serviceProvider().find(ClusterServiceFilter.ONLINE_SERVICES)) {
            if (service.group().platform().type() == PlatformType.SERVER) {
                proxyServerHandler.registerServer(service.name(), overwriteHostname(service), service.port());
            }
        }
    }

    private String overwriteHostname(@NotNull ClusterService service) {
        if (service.runningNode().equalsIgnoreCase(ClusterInstance.instance().selfService().runningNode())) {
            return "127.0.0.1";
        }
        return service.hostname();
    }

    public void unregisterPlayer(UUID uuid) {
        ClusterInstance.instance().client().sendPacket(new PlayerUnregisterPacket(uuid));
    }

    public void registerPlayer(UUID uuid, String username) {
        ClusterInstance.instance().client().sendPacket(new PlayerRegisterPacket(uuid, username, ClusterInstance.instance().selfServiceId()));
    }

    public void playerChangeServer(UUID uuid, String server) {
        ClusterInstance.instance().client().sendPacket(new PlayerServiceChangePacket(uuid, server));
    }
}
