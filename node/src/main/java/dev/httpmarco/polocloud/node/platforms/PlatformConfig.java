package dev.httpmarco.polocloud.node.platforms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class PlatformConfig {

    private final List<Platform> platforms;

}
