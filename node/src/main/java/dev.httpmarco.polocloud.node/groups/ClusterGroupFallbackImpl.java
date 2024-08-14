package dev.httpmarco.polocloud.node.groups;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.FallbackClusterGroup;
import dev.httpmarco.polocloud.api.platforms.PlatformGroupDisplay;
import org.jetbrains.annotations.NotNull;

public class ClusterGroupFallbackImpl extends ClusterGroupImpl implements FallbackClusterGroup {

    public ClusterGroupFallbackImpl(String name, PlatformGroupDisplay platform, String[] templates, String[] nodes, int minMemory, int maxMemory, boolean staticService, int minOnlineServerInstances, int maxOnlineServerInstances) {
        super(name, platform, templates, nodes, minMemory, maxMemory, staticService, minOnlineServerInstances, maxOnlineServerInstances);
    }

    public ClusterGroupFallbackImpl(@NotNull ClusterGroup group) {
        super(group.name(), group.platform(), group.templates(), group.nodes(), group.minMemory(), group.maxMemory(), group.staticService(), group.minOnlineServerInstances(), group.maxOnlineServerInstances());
    }
}
