package dev.httpmarco.polocloud.suite.services.storage;

import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.suite.cluster.storage.ClusterStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class LocalServiceStorage implements ClusterStorage<String, ClusterService> {

    private List<ClusterService> services;

    @Override
    public void initialize() {
        this.services = new CopyOnWriteArrayList<>();
    }

    @Override
    public List<ClusterService> items() {
        return this.services;
    }

    @Override
    public void publish(ClusterService item) {
        this.services.add(item);
    }

    @Override
    public ClusterService singleton(String identifier) {
        return this.services.stream().filter(it -> it.name().equalsIgnoreCase(identifier)).findFirst().orElse(null);
    }

    @Override
    public void destroy(String identifier) {
        this.services.removeIf(it -> it.name().equalsIgnoreCase(identifier));
    }

    @Override
    public String extreactIdentifier(ClusterService item) {
        return item.name();
    }
}