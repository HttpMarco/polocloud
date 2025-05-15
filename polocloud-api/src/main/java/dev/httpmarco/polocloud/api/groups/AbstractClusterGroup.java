package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.polocloud.api.platform.SharedPlatform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public abstract class AbstractClusterGroup implements ClusterGroup {

    private final String name;
    private final SharedPlatform platform;
    private final int minMemory;
    private final int maxMemory;
    private final int minOnlineService;
    private final int maxOnlineService;
    private final double percentageToStartNewService;
    private final List<String> templates;

}
