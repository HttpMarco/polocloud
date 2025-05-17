package dev.httpmarco.polocloud.common.files;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public enum FileType {

    TXT,
    TOML,
    YML,
    PROPERTIES,
    SECRET;


    private final String suffix;

    FileType() {
        this.suffix = "." + this.name().toLowerCase();
    }
}
