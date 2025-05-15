package dev.httpmarco.polocloud.instance.group;

import dev.httpmarco.polocloud.api.groups.AbstractClusterGroup;
import dev.httpmarco.polocloud.api.platform.SharedPlatform;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.explanation.Utils;

import java.util.List;

public final class ClusterInstanceGroup extends AbstractClusterGroup {

    public ClusterInstanceGroup(Utils.ClusterGroupExplanation group) {
        super(group.getName(),
                null,
                group.getMinMemory(),
                group.getMaxMemory(),
                group.getMinOnlineServices(),
                group.getMaxOnlineServices(),
                group.getPercentageToStartNewService(),
                group.getTemplatesList().stream().toList());
    }

    public ClusterInstanceGroup(String name, SharedPlatform platform, int minMemory, int maxMemory, int minOnlineService, int maxOnlineService, double percentageToStartNewService, List<String> templates) {
        super(name, platform, minMemory, maxMemory, minOnlineService, maxOnlineService, percentageToStartNewService, templates);
    }

    @Override
    public int runningServicesAmount() {
        // todo
        return 0;
    }

    @Override
    public List<ClusterService> runningServices() {
        // todo
        return List.of();
    }
}
