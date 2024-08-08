package dev.httpmarco.polocloud.plugin.spigot;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.packet.resources.services.ServiceOnlinePacket;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class SpigotPlatformBootstrap extends JavaPlugin {

    @Override
    public void onEnable() {
        // todo use self object with id
        ClusterInstance.instance().client().sendPacket(new ServiceOnlinePacket(UUID.fromString(System.getenv("serviceId"))));

        ClusterInstance.instance().groupProvider().groupsAsync().whenComplete((clusterGroups, throwable) -> {
            System.out.println("found groups async: " + clusterGroups.size());
        });

        for (ClusterGroup group : ClusterInstance.instance().groupProvider().groups()) {
            System.out.println("found group:" + group.name());
        }
    }

    @Override
    public void onDisable() {

    }
}
