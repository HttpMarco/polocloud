package dev.httpmarco.polocloud.instance.event;

import dev.httpmarco.polocloud.api.PolocloudEnvironment;
import dev.httpmarco.polocloud.api.event.Event;
import dev.httpmarco.polocloud.api.event.EventProvider;
import dev.httpmarco.polocloud.explanation.event.EventServiceGrpc;
import dev.httpmarco.polocloud.explanation.event.EventServiceOuterClass;
import io.grpc.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class EventInstanceProvider implements EventProvider {

    private final EventServiceGrpc.EventServiceBlockingStub eventServiceBlockingStub;
    private final Map<Class<? extends Event>, List<Consumer<Event>>> bindEvents = new HashMap<>();

    public EventInstanceProvider(Channel channel) {
        this.eventServiceBlockingStub = EventServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public <T extends Event> void subscribe(Class<T> eventClazz, Consumer<T> consumer) {
        var response = eventServiceBlockingStub.subscribe(EventServiceOuterClass.EventRegisterRequest.newBuilder()
                .setEventClass(eventClazz.getName())
                .setServiceId(System.getenv(PolocloudEnvironment.POLOCLOUD_SERVICE_ID.name()))
                .build());

        if (!response.getSuccess()) {
            // todo use logger
            System.err.println("Failed to subscribe to event: " + eventClazz.getName());
            return;
        }

        var events = bindEvents.getOrDefault(eventClazz, new ArrayList<>());
        events.add((Consumer<Event>) consumer);
        bindEvents.put(eventClazz, events);
    }

    @Override
    public void call(Event event) {
        this.callLocal(event);

        //todo global
    }

    public void callLocal(Event event) {
        if (!bindEvents.containsKey(event.getClass())) {
            return;
        }

        bindEvents.get(event.getClass()).forEach(consumer -> consumer.accept(event));
    }
}
