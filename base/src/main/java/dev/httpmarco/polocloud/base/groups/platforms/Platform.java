package dev.httpmarco.polocloud.base.groups.platforms;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Getter
@Accessors(fluent = true)
public abstract class Platform {

    private final Set<String> possibleVersions = new HashSet<>();

    public abstract void download(String version);

}
