package dev.httpmarco.pololcoud.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class Pair<F, S> {

    private F first;
    private S second;

}
