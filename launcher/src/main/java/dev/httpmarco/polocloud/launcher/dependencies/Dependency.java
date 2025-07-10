package dev.httpmarco.polocloud.launcher.dependencies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class Dependency {

    private final String groupId;
    private final String artifactId;
    private final String version;

    private final List<String> requiredDependencies;

}
