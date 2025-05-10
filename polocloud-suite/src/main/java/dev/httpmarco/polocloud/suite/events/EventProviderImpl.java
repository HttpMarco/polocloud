package dev.httpmarco.polocloud.suite.events;

import dev.httpmarco.polocloud.api.event.Event;
import dev.httpmarco.polocloud.api.event.EventProvider;

import java.util.function.Consumer;


public final class EventProviderImpl implements EventProvider {

    private final EventSubscribePool subscribePool = new EventSubscribePool();

    @Override
    public <T extends Event> void subscribe(Class<T> eventClazz, Consumer<T> consumer) {
        // todo



    }
}
