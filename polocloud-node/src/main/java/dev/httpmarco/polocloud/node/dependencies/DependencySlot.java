package dev.httpmarco.polocloud.node.dependencies;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class DependencySlot {

    private final List<Dependency> dependencies = new CopyOnWriteArrayList<>();

}
