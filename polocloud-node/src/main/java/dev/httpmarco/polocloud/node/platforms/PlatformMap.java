package dev.httpmarco.polocloud.node.platforms;

import dev.httpmarco.polocloud.api.Version;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PlatformMap {

    private final Version version;
    private final Platform[] platforms = new Platform[0];

}
