package dev.httpmarco.polocloud.node.dependencies.slots;

import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.common.classpath.ModifiableClassloader;
import dev.httpmarco.polocloud.node.dependencies.Dependency;
import dev.httpmarco.polocloud.node.dependencies.DependencySlot;
import dev.httpmarco.polocloud.node.dependencies.impl.RepositoryDependency;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Log4j2
@RequiredArgsConstructor
public class ClassloaderDependencySlot implements DependencySlot {

    private final ModifiableClassloader classloader;
    private final List<Dependency> dependencies = new CopyOnWriteArrayList<>();

    public void bindDependencies(@NotNull Dependency dependency) {
        this.dependencies.add(dependency);
    }

    public void bindRepositoryDependency(String groupId, String artifactId, String version) {
        this.bindDependencies(new RepositoryDependency(this, groupId, artifactId, Version.parse(version)));
    }

    public void bindRepositoryDependency(String groupId, String artifactId, String version, String classifier, String repositoryUrl) {
        this.bindDependencies(new RepositoryDependency(this, groupId, artifactId, Version.parse(version), classifier, repositoryUrl));
    }

    @Override
    public void prepare() {
        // Prepare and attach dependencies in one pass
        this.dependencies.stream()
                .peek(Dependency::prepare) // Prepare each dependency
                .parallel()
                .forEach(it -> {
                    it.initialize();
                    if (it.available()) {
                        // Attach to the classloader
                        this.rawBind(it);
                    }
                });
    }

    @Override
    public void rawBind(@NotNull Dependency dependency) {
        this.classloader.attach(dependency.file());
    }
}
