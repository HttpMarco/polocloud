package dev.httpmarco.polocloud.node.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;

@Getter
@Accessors
@AllArgsConstructor
public enum FileType {

    TOML("=", ".toml"),
    YAML(":", ".yml"),
    PROPERTIES("=", ".properties"),
    JSON(":", ".json");

    final String separator;
    final String ending;

    public static FileType define(String file) {
        return Arrays.stream(values()).filter(it -> file.toLowerCase().endsWith("." + it.ending)).findFirst().orElse(null);
    }
}