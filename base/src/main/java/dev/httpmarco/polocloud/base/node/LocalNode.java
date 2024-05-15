package dev.httpmarco.polocloud.base.node;

import dev.httpmarco.osgan.files.annotations.ConfigExclude;
import dev.httpmarco.osgan.networking.server.NettyServer;
import dev.httpmarco.osgan.networking.server.NettyServerBuilder;
import dev.httpmarco.polocloud.api.node.AbstractNode;

import java.util.UUID;

public final class LocalNode extends AbstractNode {

    @ConfigExclude
    private final NettyServer server;

    public LocalNode(UUID id, String name, String hostname, int port) {
        super(id, name, hostname, port);

        this.server = new NettyServerBuilder().withHostname(hostname).withPort(port).onActive(channelTransmit -> {

        }).onInactive(channelTransmit -> {

        }).build();
    }
}
