package dev.httpmarco.polocloud.api.logging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum LogLevel {

    OFF(""),
    INFO("&4"),
    SUCCESS("&8"),
    WARN("&5"),
    ERROR("&6");

    private final String colorCode;

}
