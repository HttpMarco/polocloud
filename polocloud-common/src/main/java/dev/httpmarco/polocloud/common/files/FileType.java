package dev.httpmarco.polocloud.common.files;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public enum FileType {

    TXT,
    PROPERTIES;


    private final String suffix;

    FileType() {
        this.suffix = "." + this.name().toLowerCase();
    }
}
