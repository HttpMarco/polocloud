package dev.httpmarco.polocloud.instance.group;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.platform.SharedPlatform;
import dev.httpmarco.polocloud.api.services.ClusterService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public final class ClusterInstanceGroup implements ClusterGroup {

    private final String name;
    private final int minMemory;
    private final int maxMemory;
    private final int minOnlineService;
    private final int maxOnlineService;
    private final double percentageToStartNewService;

    @Override
    public SharedPlatform platform() {
        return null;
    }

    @Override
    public int runningServicesAmount() {
        return 0;
    }

    @Override
    public List<ClusterService> runningServices() {
        return List.of();
    }
}
