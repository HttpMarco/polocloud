package dev.httpmarco.polocloud.api.node;

import java.util.UUID;

public interface Node {

    UUID id();

    String hostname();

    String name();

    void close();

}
