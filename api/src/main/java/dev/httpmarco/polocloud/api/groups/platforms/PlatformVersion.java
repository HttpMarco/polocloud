package dev.httpmarco.polocloud.api.groups.platforms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class PlatformVersion {

    private String version;
    private boolean proxy;

}
