package dev.httpmarco.polocloud.node.platforms;

import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.api.platform.SharedPlatform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class PlatformVersion {

    private final Platform parent;
    private final Version version;
    private final Map<String, String> urlReplacements = new HashMap<>();

    @Contract(" -> new")
    public @NotNull SharedPlatform share() {
        return new SharedPlatform(parent.name(), version, parent.type());
    }
}
