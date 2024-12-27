package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.polocloud.api.Named;

public interface ClusterGroup extends Named {

    ClusterGroupType type();

    int minMemory();

    int maxMemory();

}
