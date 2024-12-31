package dev.httpmarco.polocloud.node.dependencies;

import java.util.List;

public interface DependencyProvider {

    DependencySlot originalSlot();

    List<DependencySlot> slots();

}
