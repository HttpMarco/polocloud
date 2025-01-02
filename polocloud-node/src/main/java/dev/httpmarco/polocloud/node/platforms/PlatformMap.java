package dev.httpmarco.polocloud.node.platforms;

import dev.httpmarco.polocloud.api.Version;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public final class PlatformMap {

    private final Version version;
    private final Platform[] platforms = new Platform[0];

}
