package dev.httpmarco.polocloud.api;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public class Version {

    private final int major;
    private final int minor;
    private final int patch;

    private @Nullable String state;

    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    @Contract("_ -> new")
    public static @NotNull Version parse(@NotNull String version) {
        var parts = version.split("\\.");
        int major = Integer.parseInt(parts[0]);
        int minor = Integer.parseInt(parts[1]);
        var rawPatch = parts[2];

        if(rawPatch.contains("-")) {
            var split = rawPatch.split("-");
            return new Version(major, minor, Integer.parseInt(split[0]), split[1]);
        } else {
            return new Version(major, minor, Integer.parseInt(rawPatch));
        }
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return major + "." + minor + "." + patch + (state == null ? "" :  "-" + state);
    }

    public String semantic() {
        return major + "." + minor + "." + patch;
    }
}