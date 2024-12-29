package dev.httpmarco.polocloud.node.components.impl;

import dev.httpmarco.polocloud.node.components.Component;
import dev.httpmarco.polocloud.node.components.ComponentProvider;

import java.util.Collection;
import java.util.List;

public class ComponentProviderImpl implements ComponentProvider {

    @Override
    public Collection<Component> components() {
        return List.of();
    }
}
