package dev.httpmarco.polocloud.node.groups;

import dev.httpmarco.polocloud.api.groups.AbstractClusterGroup;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.platforms.PlatformGroupDisplay;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.node.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

public class ClusterGroupImpl extends AbstractClusterGroup {

    public ClusterGroupImpl(String name, PlatformGroupDisplay platform, String[] templates, String[] nodes, int minMemory, int maxMemory, boolean staticService, int minOnlineServerInstances, int maxOnlineServerInstances) {
        super(name, platform, templates, nodes, minMemory, maxMemory, staticService, minOnlineServerInstances, maxOnlineServerInstances);
    }
}
