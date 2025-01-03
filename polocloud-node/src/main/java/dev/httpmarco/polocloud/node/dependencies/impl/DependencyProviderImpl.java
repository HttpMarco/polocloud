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
        var snapshotMavenCentralRepo = "https://s01.oss.sonatype.org/service/local/repositories/snapshots/content/%s/%s/%s/%s-%s";

        this.originalSlot.bindRepositoryDependency("org.slf4j", "slf4j-simple", "2.0.9")
            .bindRepositoryDependency("com.google.code.gson", "gson", "2.11.0")
            .bindRepositoryDependency("io.netty", "netty5-common", "5.0.0.Alpha5")
            .bindRepositoryDependency("io.netty", "netty5-transport", "5.0.0.Alpha5")
            .bindRepositoryDependency("io.netty", "netty5-codec", "5.0.0.Alpha5")
            .bindRepositoryDependency("io.netty", "netty5-resolver", "5.0.0.Alpha5")
            .bindRepositoryDependency("io.netty", "netty5-buffer", "5.0.0.Alpha5")
            .bindRepositoryDependency("io.netty", "netty5-transport-classes-epoll", "5.0.0.Alpha5")
            .bindRepositoryDependency("dev.httpmarco", "netline", "1.0.6-SNAPSHOT", "20250102.103857-1", snapshotMavenCentralRepo);

        this.originalSlot.prepare();
    }
}
