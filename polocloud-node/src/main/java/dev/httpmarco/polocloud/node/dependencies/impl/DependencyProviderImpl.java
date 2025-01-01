package dev.httpmarco.polocloud.node.dependencies.impl;

import dev.httpmarco.polocloud.node.dependencies.DependencyProvider;
import dev.httpmarco.polocloud.node.dependencies.slots.ClassloaderDependencySlot;
import dev.httpmarco.polocloud.node.dependencies.slots.OriginalDependencySlot;
import lombok.Getter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final  class DependencyProviderImpl implements DependencyProvider {

    private final ClassloaderDependencySlot originalSlot = new OriginalDependencySlot();
    private final List<ClassloaderDependencySlot> slots = new ArrayList<>();

    @Override
    public void loadDefaults() {
        this.originalSlot.bindRepositoryDependency("com.google.code.gson", "gson", "2.8.6");
        this.originalSlot.bindRepositoryDependency("dev.httpmarco", "netline", "1.0.3-SNAPSHOT", "20241210.212738-1", "https://s01.oss.sonatype.org/service/local/repositories/snapshots/content/%s/%s/%s/%s-%s");

        this.originalSlot.prepare();
    }
}
