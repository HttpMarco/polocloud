package dev.httpmarco.polocloud.suite.dependencies;

import java.util.List;

public interface DependencyProvider {

    DependencySlot original();

    List<DependencySlot> slots();

}
