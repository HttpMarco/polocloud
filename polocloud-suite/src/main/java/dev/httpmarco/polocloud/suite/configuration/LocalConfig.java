package dev.httpmarco.polocloud.suite.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class LocalConfig {

    public static LocalConfig DEFAULT = new LocalConfig(10);

    private final int processTerminationIdleSeconds;

}
