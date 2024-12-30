package dev.httpmarco.polocloud.common.dependencies.common;

import dev.httpmarco.polocloud.common.dependencies.Dependency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.File;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public abstract class AbstractDependency implements Dependency {

    private final String name;
    private final File jarFile;

}
