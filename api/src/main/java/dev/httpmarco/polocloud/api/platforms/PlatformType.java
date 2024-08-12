package dev.httpmarco.polocloud.api.platforms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum PlatformType {

    PROXY(25565),
    SERVER(20000),
    SERVER_MASTER(30000);

    final int defaultRuntimePort;

}
