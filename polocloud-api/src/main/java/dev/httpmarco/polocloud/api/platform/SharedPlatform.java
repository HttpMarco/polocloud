package dev.httpmarco.polocloud.api.platform;

import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class SharedPlatform implements Named {

    private final String name;
    private final Version version;
    private final PlatformType type;

}
