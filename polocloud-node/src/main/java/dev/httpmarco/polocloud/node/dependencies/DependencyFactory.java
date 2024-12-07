package dev.httpmarco.polocloud.node.dependencies;

public interface DependencyFactory {

    void prepare(Dependency dependency);

    void uninstall(Dependency dependency);

}
