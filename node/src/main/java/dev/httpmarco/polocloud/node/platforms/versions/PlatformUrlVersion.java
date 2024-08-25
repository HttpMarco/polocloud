package dev.httpmarco.polocloud.node.platforms.versions;

import dev.httpmarco.polocloud.node.platforms.PlatformVersion;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class PlatformUrlVersion extends PlatformVersion {

    private final String url;

    public PlatformUrlVersion(String version, String url) {
        super(version);
        this.url = url;
    }
}
