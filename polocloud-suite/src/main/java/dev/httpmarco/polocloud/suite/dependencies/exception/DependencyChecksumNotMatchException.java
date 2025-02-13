package dev.httpmarco.polocloud.suite.dependencies.exception;

import dev.httpmarco.polocloud.suite.dependencies.Dependency;

public class DependencyChecksumNotMatchException extends RuntimeException{

    public DependencyChecksumNotMatchException(Dependency dependency) {
        super("Dependency checksum not match: " + dependency.name());
    }
}
