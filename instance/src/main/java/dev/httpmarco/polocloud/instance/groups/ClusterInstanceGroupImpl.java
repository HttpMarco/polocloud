package dev.httpmarco.polocloud.instance.groups;

import dev.httpmarco.polocloud.api.groups.AbstractClusterGroup;
import dev.httpmarco.polocloud.api.packet.resources.group.GroupRequestUpdatePacket;
import dev.httpmarco.polocloud.api.platforms.PlatformGroupDisplay;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.instance.ClusterInstance;

public final class ClusterInstanceGroupImpl extends AbstractClusterGroup {

    public ClusterInstanceGroupImpl(String name, PlatformGroupDisplay platform, String[] templates, String[] nodes, int maxMemory, int maxPlayers, boolean staticService, int minOnlineServerInstances, int maxOnlineServerInstances, PropertiesPool properties) {
        super(name, platform, templates, nodes, maxMemory, maxPlayers, staticService, minOnlineServerInstances, maxOnlineServerInstances, properties);
    }

    @Override
    public void update() {
        ClusterInstance.instance().client().sendPacket(new GroupRequestUpdatePacket(this));
    }
}
