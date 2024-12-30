package dev.httpmarco.polocloud.common.dependencies.impl;

import dev.httpmarco.polocloud.common.dependencies.common.AbstractDependency;

public class ExternalDependency extends AbstractDependency {

    private final String groupId;
    private final String artifactId;

    public ExternalDependency(String groupId, String artifactId, String version) {
        super(artifactId + "-" + version + ".jar", null);

        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    @Override
    public void prepare() {
        // todo download
    }
}
