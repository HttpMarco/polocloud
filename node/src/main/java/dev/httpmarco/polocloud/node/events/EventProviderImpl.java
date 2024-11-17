package dev.httpmarco.polocloud.node.events;

import dev.httpmarco.polocloud.api.event.*;
import dev.httpmarco.polocloud.api.packet.resources.event.EventCallPacket;
import dev.httpmarco.polocloud.api.packet.resources.event.EventSubscribePacket;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.services.ClusterLocalServiceImpl;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@Slf4j
@Getter
@Accessors(fluent = true)
public final class EventProviderImpl implements EventProvider {

    private final EventSubscribePool pool = new EventSubscribePool("local");
    private final EventFactory factory = new EventFactoryImpl();

    public EventProviderImpl() {
        Node.instance().server().listen(EventCallPacket.class, (transmit, packet) -> {
            if (Node.instance().serviceProvider().isServiceChannel(transmit)) {
                factory.call(packet);
                return;
            }

            for (var pool : EventPoolRegister.pools()) {
                pool.acceptActor(packet);
            }
        });

        Node.instance().server().listen(EventSubscribePacket.class, (transmit, packet) -> {
            var service = Node.instance().serviceProvider().find(transmit);

            if (service instanceof ClusterLocalServiceImpl localService) {
                // register a binding on the packet for transmit the event as packet to the service
                localService.eventSubscribePool().subscribe(packet.packetClass(), callPacket -> localService.transmit().sendPacket(callPacket));
            } else {
                log.warn("Service try to subscribe the event {} but only local service can do this!", packet.getClass());
                log.warn("Break subscription of the event.");
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> void listen(@NotNull Class<T> eventClazz, Consumer<T> event) {
        pool.subscribe(eventClazz.getName(), event1 -> {
            try {
                event.accept((T) event1.buildEvent());
            } catch (ClassNotFoundException ignored) {
            }
        });
    }
}
