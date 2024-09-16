package dev.httpmarco.polocloud.addons.proxy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class Tablist {

    private final String header;
    private final String footer;

}
