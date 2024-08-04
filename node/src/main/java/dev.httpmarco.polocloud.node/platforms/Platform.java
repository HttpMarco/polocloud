package dev.httpmarco.polocloud.node.platforms;

import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * paper
 * velocity
 * purpur
 * spigot
 * bungeecord
 * sponge powered
 * minestom
 * multipaper
 * fabric
 * (folia)
 */

@Setter
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public final class Platform {

    public static final String DEFAULT_SHUTDOWN_COMMAND = "stop";

    private final String platform;
    private final PlatformType type;
    private final Set<PlatformVersion> versions;
    private final String shutdownCommand;

    private @Nullable PlatformPatcher platformPatcher;
    private @Nullable String[] startArguments;

}
