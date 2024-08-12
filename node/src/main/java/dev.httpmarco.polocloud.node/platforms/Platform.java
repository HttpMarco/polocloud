package dev.httpmarco.polocloud.node.platforms;

import dev.httpmarco.polocloud.api.platforms.PlatformType;
import dev.httpmarco.polocloud.node.platforms.actions.PlatformAction;
import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    private final List<PlatformAction> actions = new ArrayList<>();

    private @Nullable PlatformPatcher platformPatcher;
    private @Nullable String[] startArguments;

}
