package dev.httpmarco.polocloud.node.dependencies.exceptions;

import dev.httpmarco.polocloud.node.dependencies.xml.DependencyScheme;

public final class UnkownDependencyVersionException extends Exception {

    public UnkownDependencyVersionException(DependencyScheme scheme) {
        super("Unable to determine latest stable version for " + scheme.groupId() + ":" + scheme.artifactId());
    }
}
