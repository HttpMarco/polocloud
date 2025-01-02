package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.groups.instance.ClusterGroupServiceBuilder;

public interface ClusterGroup extends Named {

    ClusterGroupType type();

    int minMemory();

    int maxMemory();

    ClusterGroupServiceBuilder newInstanceBuilder();

}
