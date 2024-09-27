package dev.httpmarco.polocloud.addons.proxy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class Motd {

    private final String firstLine;
    private final String secondLine;

    @Override
    public String toString() {
        return this.firstLine + "\n" + secondLine;
    }
}
