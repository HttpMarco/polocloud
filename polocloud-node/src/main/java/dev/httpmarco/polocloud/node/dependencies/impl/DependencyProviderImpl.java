package dev.httpmarco.polocloud.node.dependencies.impl;

import dev.httpmarco.polocloud.node.dependencies.Dependency;
import dev.httpmarco.polocloud.node.dependencies.DependencyFactory;
import dev.httpmarco.polocloud.node.dependencies.DependencyProvider;
import lombok.Getter;
import lombok.experimental.Accessors;
import java.util.LinkedList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class DependencyProviderImpl implements DependencyProvider {

    private final List<Dependency> loadedDependencies = new LinkedList<>();
    private final DependencyFactory factory;

    public DependencyProviderImpl() {
        this.factory = new DependencyFactoryImpl(this);
    }

    @Override
    public void registerDependency(Dependency dependency) {
        factory.prepare(dependency);
    }

    @Override
    public void unregisterDependency(Dependency dependency) {
        factory.uninstall(dependency);
    }
}
