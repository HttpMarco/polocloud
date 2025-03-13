package dev.httpmarco.polocloud.suite.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class Pair<L, R> {

    private final L left;
    private final R right;

}
