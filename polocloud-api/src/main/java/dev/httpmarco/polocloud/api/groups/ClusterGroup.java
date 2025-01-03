package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.groups.instance.ClusterGroupServiceBuilder;
import dev.httpmarco.polocloud.api.platform.SharedPlatform;

public interface ClusterGroup extends Named {

    SharedPlatform platform();

    int minMemory();

    int maxMemory();

    ClusterGroupServiceBuilder newInstanceBuilder();

}
