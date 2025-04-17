package dev.httpmarco.polocloud.suite.groups.storage;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;

import java.util.List;

public final class GlobalClusterGroupStorage implements ClusterGroupStorage {

    @Override
    public void initialize() {
        // todo
    }

    @Override
    public List<ClusterGroup> groups() {
        // todo
        return List.of();
    }

    @Override
    public void publish(ClusterGroup group) {
        // todo

    }

    @Override
    public ClusterGroup singleton(String identifier) {
        // todo
        return null;
    }

    @Override
    public void destroy(String identifier) {

    }
}
