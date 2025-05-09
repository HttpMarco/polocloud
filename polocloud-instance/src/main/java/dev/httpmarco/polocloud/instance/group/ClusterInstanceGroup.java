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

    private String name;

    @Override
    public SharedPlatform platform() {
        return null;
    }

    @Override
    public int minMemory() {
        return 0;
    }

    @Override
    public int maxMemory() {
        return 0;
    }

    @Override
    public int minOnlineService() {
        return 0;
    }

    @Override
    public int maxOnlineService() {
        return 0;
    }

    @Override
    public double percentageToStartNewService() {
        return 0;
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
