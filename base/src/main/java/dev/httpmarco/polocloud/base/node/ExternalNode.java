package dev.httpmarco.polocloud.base.node;

import dev.httpmarco.polocloud.api.node.Node;

import java.util.UUID;

public record ExternalNode(UUID id, String name, String hostname, int port) implements Node {

    @Override
    public void close() {

    }
}
