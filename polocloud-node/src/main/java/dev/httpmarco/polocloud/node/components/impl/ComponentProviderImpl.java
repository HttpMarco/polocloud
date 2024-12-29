package dev.httpmarco.polocloud.node.components.impl;

import dev.httpmarco.polocloud.node.components.ComponentContainer;
import dev.httpmarco.polocloud.node.components.ComponentInfoSnapshot;
import dev.httpmarco.polocloud.node.components.ComponentProvider;

import java.util.Collection;
import java.util.List;

public class ComponentProviderImpl implements ComponentProvider {

    @Override
    public Collection<ComponentContainer> containers() {
        return List.of();
    }

    @Override
    public ComponentContainer find(ComponentInfoSnapshot snapshot) {
        return null;
    }

    @Override
    public ComponentContainer find(String name) {
        return null;
    }
}
