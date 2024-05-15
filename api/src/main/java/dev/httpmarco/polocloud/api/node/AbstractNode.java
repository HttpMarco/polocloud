package dev.httpmarco.polocloud.api.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public abstract class AbstractNode implements Node {

    private final UUID id;
    private final String name;
    private final String hostname;
    private final int port;

}
