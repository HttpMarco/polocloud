package dev.httpmarco.polocloud.node.dependencies.exceptions;

import dev.httpmarco.polocloud.node.dependencies.xml.DependencyScheme;
import org.jetbrains.annotations.NotNull;

public final class UnkownDependencyVersionException extends Exception {

    public UnkownDependencyVersionException(@NotNull DependencyScheme scheme) {
        super("Unable to determine latest stable version for " + scheme.groupId() + ":" + scheme.artifactId());
    }
}
