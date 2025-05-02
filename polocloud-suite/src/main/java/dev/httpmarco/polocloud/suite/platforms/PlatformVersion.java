package dev.httpmarco.polocloud.suite.platforms;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class PlatformVersion {

    private final String version;
    private final String buildId;

}
