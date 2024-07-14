package dev.httpmarco.polocloud.base.node;

import dev.httpmarco.osgan.networking.client.CommunicationClient;
import dev.httpmarco.polocloud.api.node.Node;
import dev.httpmarco.pololcoud.common.document.DocumentIgnore;
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

    @DocumentIgnore
    private final Node headNode;

    private String id;
    private String token;
    private Set<Node> endpoints;

    public boolean hasEndpoint(String id, String hostname, int port) {
        return endpoints.stream().anyMatch(node -> node.id().equals(id) || (node.hostname().equals(hostname) && node.port() == port));
    }

    public void connectToEndpoints() {
        for (var endpoint : endpoints) {
            var client = new CommunicationClient(endpoint.hostname(), endpoint.port());
            // todo
        }
    }
}
