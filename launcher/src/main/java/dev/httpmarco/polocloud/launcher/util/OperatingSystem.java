package dev.httpmarco.polocloud.launcher.util;

public enum OperatingSystem {

    WINDOWS,
    LINUX,
    MAC,
    UNIX,
    UNKNOWN;

    public static OperatingSystem detectOS() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            return WINDOWS;
        } else if (osName.contains("nix") || osName.contains("nux")) {
            return LINUX;
        } else if (osName.contains("mac")) {
            return MAC;
        } else if (osName.contains("sunos") || osName.contains("unix")) {
            return UNIX;
        } else {
            return UNKNOWN;
        }
    }
}
