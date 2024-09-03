package dev.httpmarco.polocloud.node.groups;

import dev.httpmarco.polocloud.api.groups.AbstractClusterGroup;
import dev.httpmarco.polocloud.api.platforms.PlatformGroupDisplay;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;

public class ClusterGroupImpl extends AbstractClusterGroup {

    public ClusterGroupImpl(String name, PlatformGroupDisplay platform, String[] templates, String[] nodes, int maxMemory, int maxPlayers, boolean staticService, int minOnlineServerInstances, int maxOnlineServerInstances, PropertiesPool properties) {
        super(name, platform, templates, nodes, maxMemory, maxPlayers, staticService, minOnlineServerInstances, maxOnlineServerInstances, properties);
    }
}
