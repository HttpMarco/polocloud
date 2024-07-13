package dev.httpmarco.polocloud.base.node;

import dev.httpmarco.osgan.files.DocumentExclude;
import dev.httpmarco.polocloud.api.node.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Accessors(fluent = true)
public class Cluster {

    @DocumentExclude
    private final Node headNode;

    private String id;
    private String token;
    private Set<Node> endpoints;

    public boolean hasEndpoint(String id, String hostname, int port) {
        return endpoints.stream().anyMatch(node -> node.id().equals(id) || (node.hostname().equals(hostname) && node.port() == port));
    }

}
