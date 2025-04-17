package dev.httpmarco.polocloud.api;

public class Version {

    private final String originalVersion;
    private final int major;
    private final String minor;
    private final String patch;
    private final String numberId;
    private final String state;

    public Version(String originalVersion, int major, String minor, String patch, String numberId, String state) {
        this.originalVersion = originalVersion;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.state = state;
        this.numberId = numberId;
    }

    public static Version parse(String version) {
        // Split the version string into major, minor, patch, and optional state
        var mainParts = version.split("-", 2);
        var versionParts = mainParts[0].split("\\.", -1);

        var major = Integer.parseInt(parseOrDefault(versionParts, 0));

        String minor = null;
        if (versionParts.length > 1) {
            minor = parseOrDefault(versionParts, 1);
        }

        String patch = null;
        if (versionParts.length > 2) {
            patch = parseOrDefault(versionParts, 2);
        }

        String numberId = null;
        if (versionParts.length > 3) {
            numberId = String.valueOf(parseOrDefault(versionParts, 3));
        }

        var state = (mainParts.length > 1) ? mainParts[1] : null;
        return new Version(version, major, minor, patch, numberId, state);
    }

    private static String parseOrDefault(String[] parts, int index) {
        return (index < parts.length) ? parts[index] : "0";
    }

    @Override
    public String toString() {
        return originalVersion;
    }

    public String versionWithState() {
        var builder = new StringBuilder();
        builder.append(major);

        if(minor != null) {
            builder.append(".").append(minor);
        }

        if (patch != null) {
            builder.append(".").append(patch);
        }
        if (numberId != null) {
            builder.append(".").append(numberId);
        }
        if (state != null) {
            builder.append("-").append(state);
        }
        return builder.toString();
    }
}
