package dev.httpmarco.polocloud.instance.groups;

import dev.httpmarco.polocloud.api.groups.AbstractClusterGroup;
import dev.httpmarco.polocloud.api.platforms.PlatformGroupDisplay;

public final class ClusterInstanceGroupImpl extends AbstractClusterGroup {

    public ClusterInstanceGroupImpl(String name, PlatformGroupDisplay platform, String[] templates, String[] nodes, int minMemory, int maxMemory, boolean staticService, int minOnlineServerInstances, int maxOnlineServerInstances) {
        super(name, platform, templates, nodes, minMemory, maxMemory, staticService, minOnlineServerInstances, maxOnlineServerInstances);
    }
}
