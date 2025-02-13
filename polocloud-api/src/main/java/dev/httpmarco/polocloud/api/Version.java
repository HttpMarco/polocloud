package dev.httpmarco.polocloud.api;

public class Version {

    private final String originalVersion;

    private final int major;
    private final int minor;
    private final int patch;

    private String state;

    public Version(String originalVersion, int major, int minor, int patch, String state) {
        this.originalVersion = originalVersion;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.state = state;
    }

    public static Version parse(String version) {
        // Split the version string into major, minor, patch, and optional state
        var mainParts = version.split("-", 2);
        var versionParts = mainParts[0].split("\\.");

        var major = parseOrDefault(versionParts, 0);
        var minor = parseOrDefault(versionParts, 1);
        var patch = parseOrDefault(versionParts, 2);
        var state = (mainParts.length > 1) ? mainParts[1] : null;

        return new Version(version, major, minor, patch, state);
    }

    private static int parseOrDefault(String[] parts, int index) {
        return (index < parts.length) ? Integer.parseInt(parts[index]) : 0;
    }

    @Override
    public String toString() {
        return originalVersion;
    }

    public String semantic() {
        return major + "." + minor + "." + patch;
    }
}
