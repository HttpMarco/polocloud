package dev.httpmarco.polocloud.node.dependencies;

import dev.httpmarco.polocloud.common.ModifiableClassloader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Log4j2
@RequiredArgsConstructor
public final class DependencySlot {

    private final ModifiableClassloader classloader;
    private final List<Dependency> dependencies = new CopyOnWriteArrayList<>();

    public void bindDependencies(@NotNull Dependency dependency) {
        this.dependencies.add(dependency);
    }

    public void prepare() {
        this.dependencies.stream().parallel().forEach(it -> {
            it.prepare();
            if(!it.available()) {
                log.warn("Cannot bind dependencies for {}", it);
                return;
            }
            this.classloader.attach(it.file());
        });
    }
}
