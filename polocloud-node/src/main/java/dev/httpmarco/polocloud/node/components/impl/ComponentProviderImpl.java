package dev.httpmarco.polocloud.node.components.impl;

import dev.httpmarco.polocloud.node.components.ComponentContainer;
import dev.httpmarco.polocloud.node.components.ComponentInfoSnapshot;
import dev.httpmarco.polocloud.node.components.ComponentProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class ComponentProviderImpl implements ComponentProvider {

    private final List<ComponentContainer> containers = new ArrayList<>();

    @Contract(pure = true)
    @Override
    public @NotNull @Unmodifiable Collection<ComponentContainer> containers() {
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
