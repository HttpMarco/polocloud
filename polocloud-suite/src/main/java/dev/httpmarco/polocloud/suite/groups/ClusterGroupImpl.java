package dev.httpmarco.polocloud.suite.groups;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.platform.SharedPlatform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class ClusterGroupImpl implements ClusterGroup {

    private final String name;
    private final SharedPlatform platform;
    private int minMemory;
    private int maxMemory;

}
