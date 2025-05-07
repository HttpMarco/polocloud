package dev.httpmarco.polocloud.suite.dependencies;

import java.util.List;

public interface DependencySlot {

    void append(Dependency dependency);

    List<Dependency> bindDependencies();

}
