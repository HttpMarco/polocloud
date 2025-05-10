package dev.httpmarco.polocloud.suite.groups;

import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.platform.SharedPlatform;
import dev.httpmarco.polocloud.api.services.ClusterService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class ClusterGroupImpl implements ClusterGroup {

    private final String name;
    private final SharedPlatform platform;
    private int minMemory;
    private int maxMemory;
    private int minOnlineService;
    private int maxOnlineService;
    private double percentageToStartNewService;
    private List<String> templates;

    @Override
    public int runningServicesAmount() {
        return this.runningServices().size();
    }

    @Override
    public List<ClusterService> runningServices() {
        return Polocloud.instance().serviceProvider().findAll().stream().filter(it -> it.group().equals(this)).toList();
    }
}
