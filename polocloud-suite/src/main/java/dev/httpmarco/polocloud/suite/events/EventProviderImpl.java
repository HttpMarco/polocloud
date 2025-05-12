package dev.httpmarco.polocloud.suite.events;

import dev.httpmarco.polocloud.api.event.Event;
import dev.httpmarco.polocloud.api.event.EventProvider;
import dev.httpmarco.polocloud.suite.events.pool.EventSubscribePool;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


@Accessors(fluent = true)
@Log4j2
public final class EventProviderImpl implements EventProvider {

    @Getter
    private final EventSubscribePool subscribePool = new EventSubscribePool();
    private final Map<Class<? extends Event>, List<Consumer<Event>>> localSubscribeEvents = new HashMap<>();

    @Override
    public <T extends Event> void subscribe(Class<T> eventClazz, Consumer<T> consumer) {
        var subscribeList = localSubscribeEvents.getOrDefault(eventClazz, new ArrayList<>());
        subscribeList.add((Consumer<Event>) consumer);
        localSubscribeEvents.put(eventClazz, subscribeList);
        log.debug("subscribe new event: {}", eventClazz);
    }

    @Override
    public void call(Event event) {
        this.callLocal(event);
        subscribePool.callGlobal(event);
        log.info("called event: {}", event.getClass());
    }

    public void callLocal(Event event) {
        if (!localSubscribeEvents.containsKey(event.getClass())) {
            return;
        }
        this.localSubscribeEvents.get(event.getClass()).forEach(consumer -> consumer.accept(event));
    }
}
