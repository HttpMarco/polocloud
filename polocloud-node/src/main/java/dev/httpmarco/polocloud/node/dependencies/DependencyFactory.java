package dev.httpmarco.polocloud.node.dependencies;

import java.util.List;

public interface DependencyFactory {

    void prepare(Dependency dependency);

    void uninstall(Dependency dependency);

    List<Dependency> loadSubDependencies(Dependency dependency);

}
