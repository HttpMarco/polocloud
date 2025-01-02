package dev.httpmarco.polocloud.api.groups.instance;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import org.jetbrains.annotations.NotNull;

public abstract class ClusterGroupServiceBuilder {

    private final ClusterGroup group;
    public int maxMemory;

    public ClusterGroupServiceBuilder(@NotNull ClusterGroup group) {
        this.group = group;
        this.maxMemory = group.maxMemory();
    }

    public abstract void startService();

    public void maxMemory(int maxMemory) {
        this.maxMemory = maxMemory;
    }
}