package dev.httpmarco.polocloud.instance;

import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;

public class PolocloudInstance extends Polocloud {

    @Override
    public ClusterServiceProvider serviceProvider() {
        return null;
    }

    @Override
    public ClusterGroupProvider groupProvider() {
        return null;
    }
}
