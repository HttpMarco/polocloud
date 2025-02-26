package dev.httpmarco.polocloud.instance;

import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;

public class PolocloudInstance extends Polocloud {


    @Override
    public ClusterGroupProvider groupProvider() {
        return null;
    }
}
