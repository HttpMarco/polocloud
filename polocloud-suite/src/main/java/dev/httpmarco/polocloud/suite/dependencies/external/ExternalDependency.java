package dev.httpmarco.polocloud.suite.dependencies.external;

import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.suite.dependencies.Dependency;
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

    @Override
    public File file() {
        return DependencyProviderImpl.DEPENDENCY_DIRECTORY.resolve(artifactId + "-" + version + ".jar").toFile();
    }

    @Override
    public String name() {
        return groupId;
    }
}
