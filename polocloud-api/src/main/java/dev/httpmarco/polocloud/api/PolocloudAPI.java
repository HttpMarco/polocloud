package dev.httpmarco.polocloud.api;

import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;

public abstract class PolocloudAPI {

    public abstract ClusterGroupProvider groupProvider();

}
