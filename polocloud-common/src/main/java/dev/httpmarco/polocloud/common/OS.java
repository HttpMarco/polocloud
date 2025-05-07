package dev.httpmarco.polocloud.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum OS {

    WINDOWS(";"),
    LINUX(":"),
    MACOS(":"),
    UNKNOWN(":");

    private final String processSeparator;

    public static OS detect() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            return WINDOWS;
        } else if (osName.contains("nux") || osName.contains("nix")) {
            return LINUX;
        } else if (osName.contains("mac")) {
            return MACOS;
        } else {
            return UNKNOWN;
        }
    }
}
