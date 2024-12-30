package dev.httpmarco.polocloud.api;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Version(int major, int minor, int patch) {

    @Contract("_ -> new")
    public static @NotNull Version parse(@NotNull String version) {
        var parts = version.split("\\.");
        return new Version(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }
}