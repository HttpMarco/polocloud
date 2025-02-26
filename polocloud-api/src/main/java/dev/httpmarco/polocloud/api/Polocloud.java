package dev.httpmarco.polocloud.api;

import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;

public abstract class Polocloud {

    private static Polocloud instance;

    private ClusterGroupProvider groupProvider;

    public Polocloud() {
        instance = this;
    }

    public static Polocloud instance() {
        return instance;
    }

    public abstract ClusterGroupProvider groupProvider();
}
