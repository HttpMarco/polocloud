package dev.httpmarco.polocloud.node.cluster;

import dev.httpmarco.osgan.networking.server.CommunicationServer;

public interface LocalNode extends NodeEndpoint {

    CommunicationServer server();

    void initialize();

    String localServiceBindingAddress();

}
