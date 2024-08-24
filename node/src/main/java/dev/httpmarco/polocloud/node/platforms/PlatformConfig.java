package dev.httpmarco.polocloud.node.platforms;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class PlatformConfig {

    private final List<Platform> platforms = new ArrayList<>();

}
