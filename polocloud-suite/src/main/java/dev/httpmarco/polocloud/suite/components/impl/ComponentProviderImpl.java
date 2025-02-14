package dev.httpmarco.polocloud.suite.components.impl;

import dev.httpmarco.polocloud.suite.components.ComponentContainer;
import dev.httpmarco.polocloud.suite.components.ComponentInfoSnapshot;
import dev.httpmarco.polocloud.suite.components.ComponentProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class ComponentProviderImpl implements ComponentProvider {

    private final List<ComponentContainer> containers = new ArrayList<>();

    @Override
    public Collection<ComponentContainer> containers() {
        return List.copyOf(containers);
    }

    @Override
    public ComponentContainer find(ComponentInfoSnapshot snapshot) {
        return this.containers.stream().filter(it -> it.snapshot().equals(snapshot)).findFirst().orElse(null);
    }

    @Override
    public ComponentContainer find(String name) {
        return this.containers.stream().filter(it -> it.snapshot().id().equals(name)).findFirst().orElse(null);
    }
}

