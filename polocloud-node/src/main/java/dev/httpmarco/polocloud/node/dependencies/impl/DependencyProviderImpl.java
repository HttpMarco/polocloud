package dev.httpmarco.polocloud.node.dependencies.impl;

import dev.httpmarco.polocloud.common.classpath.ModifiableClassloader;
import dev.httpmarco.polocloud.node.dependencies.DependencyProvider;
import dev.httpmarco.polocloud.node.dependencies.DependencySlot;
import lombok.Getter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final  class DependencyProviderImpl implements DependencyProvider {

    private final DependencySlot originalSlot = new DependencySlot(ModifiableClassloader.defaultClassLoader());
    private final List<DependencySlot> slots = new ArrayList<>();

    @Override
    public void loadDefaults() {
        this.originalSlot.bindRepositoryDependency("com.google.code.gson", "gson", "2.8.6");
        this.originalSlot.prepare();
    }
}
