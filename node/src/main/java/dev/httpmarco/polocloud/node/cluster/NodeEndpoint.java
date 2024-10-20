package dev.httpmarco.polocloud.node.cluster;

import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.osgan.networking.channel.ChannelTransmit;
import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.polocloud.api.Closeable;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public interface NodeEndpoint extends Closeable {

    NodeSituation situation();

    NodeEndpointData data();

    @Nullable ChannelTransmit transmit();

    void situation(NodeSituation situation);

    default <P extends Packet> P request(String id, Class<P> packet, CommunicationProperty property) {
        return this.requestAsync(id, packet, property).join();
    }

    default <P extends Packet> P request(String id, Class<P> packet) {
        return this.request(id, packet, new CommunicationProperty());
    }

    default <P extends Packet> CompletableFuture<P> requestAsync(String id, Class<P> packet) {
        return this.requestAsync(id, packet, new CommunicationProperty());
    }

    <P extends Packet> CompletableFuture<P> requestAsync(String id, Class<P> packet, CommunicationProperty property);

}
