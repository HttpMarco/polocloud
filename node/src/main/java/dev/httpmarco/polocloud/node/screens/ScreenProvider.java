package dev.httpmarco.polocloud.node.screens;

import dev.httpmarco.polocloud.api.Closeable;
import dev.httpmarco.polocloud.api.packet.resources.screen.ExternalScreenSubscribePacket;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.cluster.NodeEndpoint;
import dev.httpmarco.polocloud.node.services.ClusterLocalServiceImpl;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Accessors(fluent = true)
public final class ScreenProvider implements Closeable {

    private @Nullable ClusterService current;
    private final Map<NodeEndpoint, ClusterService> externalSubscribes = new HashMap<>();

    public ScreenProvider() {
        Node.instance().server().listen(ExternalScreenSubscribePacket.class, (transmit, packet) -> {
            var service = Node.instance().serviceProvider().find(packet.id());

            if (service instanceof ClusterLocalServiceImpl localService) {

            } else {
                log.warn("Screens request failed&8! &f{} &7is not a valid local service.", service.name());
            }
        });

    }

    public void display(ClusterService service) {
        if (this.current != null) {
            // todo call unsubscribe packet
        }
        this.current = service;
        if (!(service instanceof ClusterLocalServiceImpl)) {
            NodeEndpoint endpoint = Node.instance().clusterProvider().find(service.runningNode());

            // send subscription for screen logging
            endpoint.transmit().sendPacket(new ExternalScreenSubscribePacket(service.id()));
        }

        Node.instance().terminal().clear();

        for (var log : service.logs()) {
            Node.instance().terminal().printLine(log);
        }
    }

    public void publish(ClusterService service, List<String> appendLogLines) {
        if (service.equals(current)) {
            for (String appendLogLine : appendLogLines) {
                Node.instance().terminal().printLine(appendLogLine);
            }
        }

        for (var endpoint : externalSubscribes.keySet()) {
            if (externalSubscribes.get(endpoint).equals(service)) {
                //todo send new log
            }
        }
    }

    public void redraw() {
        if (!(current instanceof ClusterLocalServiceImpl)) {
            // todo send unsubscribe packet
        }

        this.current = null;
        Node.instance().terminal().clear();
    }

    public boolean isUsed() {
        return this.current != null;
    }

    @Override
    public void close() {
        this.current = null;
        // todo unsubscribe
    }
}
