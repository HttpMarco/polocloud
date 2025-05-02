package dev.httpmarco.polocloud.suite.platforms;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class PlatformVersion {

    private final String version;
    private final String buildId;

}
