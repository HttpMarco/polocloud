package dev.httpmarco.polocloud.node.platforms.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public final class PlatformFileReplacement {

    private String indicator;
    private String value;

}
