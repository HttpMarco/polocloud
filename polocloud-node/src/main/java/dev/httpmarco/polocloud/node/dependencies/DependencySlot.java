package dev.httpmarco.polocloud.node.dependencies;

import dev.httpmarco.polocloud.api.Available;
import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.common.classpath.ModifiableClassloader;
import dev.httpmarco.polocloud.node.dependencies.impl.RepositoryDependency;
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

    public void bindRepositoryDependency(String groupId, String artifactId, String version) {
        this.bindDependencies(new RepositoryDependency(this, groupId, artifactId, Version.parse(version)));
    }

    public void prepare() {
        // Prepare and attach dependencies in one pass
        this.dependencies.stream()
                .peek(Dependency::prepare) // Prepare each dependency
                .parallel()
                .filter(Available::available) // Filter available dependencies
                .forEach(it -> {
                    it.initialize();
                    // Attach to the classloader
                    this.classloader.attach(it.file());
                });
    }
}
