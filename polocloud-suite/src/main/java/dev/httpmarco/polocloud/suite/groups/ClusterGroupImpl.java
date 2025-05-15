package dev.httpmarco.polocloud.suite.groups;

import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.api.groups.AbstractClusterGroup;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.platform.SharedPlatform;
import dev.httpmarco.polocloud.api.services.ClusterService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Accessors(fluent = true)
public final class ClusterGroupImpl extends AbstractClusterGroup {

    public ClusterGroupImpl(String name, SharedPlatform platform, int minMemory, int maxMemory, int minOnlineService, int maxOnlineService, double percentageToStartNewService, List<String> templates) {
        super(name, platform, minMemory, maxMemory, minOnlineService, maxOnlineService, percentageToStartNewService, templates);
    }

    @Override
    public int runningServicesAmount() {
        return this.runningServices().size();
    }

    @Override
    public List<ClusterService> runningServices() {
        return Polocloud.instance().serviceProvider().findAll().stream().filter(it -> it.group().equals(this)).toList();
    }
}
