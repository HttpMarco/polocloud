package dev.httpmarco.polocloud.node.dependencies;

import java.util.Collection;

public interface DependencyProvider {

    DependencyFactory factory();

    Collection<Dependency> loadedDependencies();

    void registerDependency(Dependency dependency);

    void unregisterDependency(Dependency dependency);

}
