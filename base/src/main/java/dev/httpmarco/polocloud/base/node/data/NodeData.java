package dev.httpmarco.polocloud.base.node.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class NodeData {

    private final String id;
    private final String hostname;
    private final int port;

}
