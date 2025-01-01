package dev.httpmarco.polocloud.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
public class Version {

    private final String originalVersion;

    private final int major;
    private final int minor;
    private final int patch;

    private @Nullable String state;

    /**
     * Parse a version string into a Version object.
     *
     * @param version the version string (e.g., "5.9.0-Alpha5", "1.2.3", "2.17", "3")
     * @return the parsed Version object
     */
    @Contract("_ -> new")
    public static @NotNull Version parse(@NotNull String version) {
        // Split the version string into major, minor, patch, and optional state
        String[] mainParts = version.split("-", 2);
        String[] versionParts = mainParts[0].split("\\.");

        int major = parseOrDefault(versionParts, 0, 0);
        int minor = parseOrDefault(versionParts, 1, 0); // Default to 0 if not provided
        int patch = parseOrDefault(versionParts, 2, 0); // Default to 0 if not provided
        String state = (mainParts.length > 1) ? mainParts[1] : null;

        return new Version(version, major, minor, patch, state);
    }

    private static int parseOrDefault(String[] parts, int index, int defaultValue) {
        return (index < parts.length) ? Integer.parseInt(parts[index]) : defaultValue;
    }

    @Override
    public @NotNull String toString() {
        return originalVersion;
    }

    public String semantic() {
        return major + "." + minor + "." + patch;
    }
}