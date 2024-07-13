package dev.httpmarco.polocloud.api.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public class Node {

    private String id;
    private String hostname;
    private int port;

}
