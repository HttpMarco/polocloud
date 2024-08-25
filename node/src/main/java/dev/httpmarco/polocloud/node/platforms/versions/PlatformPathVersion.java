package dev.httpmarco.polocloud.node.platforms.versions;

import dev.httpmarco.polocloud.node.platforms.PlatformVersion;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class PlatformPathVersion extends PlatformVersion {

    private final String fileName;

    public PlatformPathVersion(String version, String fileName) {
        super(version);
        this.fileName = fileName;
    }
}
