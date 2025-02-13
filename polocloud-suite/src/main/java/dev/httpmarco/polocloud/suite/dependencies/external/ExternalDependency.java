package dev.httpmarco.polocloud.suite.dependencies.external;

import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.suite.dependencies.Dependency;
import dev.httpmarco.polocloud.suite.dependencies.DependencySlot;
import dev.httpmarco.polocloud.suite.dependencies.external.pom.Pom;
import dev.httpmarco.polocloud.suite.dependencies.impl.DependencyProviderImpl;

import java.io.File;

public final class ExternalDependency implements Dependency {

    private final String groupId;
    private final String artifactId;
    private final Version version;

    private final Pom pom;

    public ExternalDependency(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = Version.parse(version);

        this.pom = new Pom(this);
    }

    public void load(DependencySlot slot) {
        for (ExternalDependency dependency : pom.dependencies()) {
            dependency.load(slot);
        }
        slot.append(this);
    }

    @Override
    public File file() {
        return DependencyProviderImpl.DEPENDENCY_DIRECTORY.resolve(artifactId + "-" + version + ".jar").toFile();
    }

    public String version() {
        return this.version.semantic();
    }

    public String artifactId() {
        return this.artifactId;
    }

    public String groupIdPath() {
        return this.groupId.replace('.', '/');
    }

    @Override
    public String name() {
        return groupId;
    }
}
