package dev.httpmarco.polocloud.suite.events;

import dev.httpmarco.polocloud.api.event.AbstractLocalEventProvider;
import dev.httpmarco.polocloud.api.event.Event;
import dev.httpmarco.polocloud.suite.events.pool.EventSubscribePool;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


@Accessors(fluent = true)
@Log4j2
public final class EventProviderImpl extends AbstractLocalEventProvider {

    @Getter
    private final EventSubscribePool subscribePool = new EventSubscribePool();

    @Override
    public <T extends Event> void subscribe(Class<T> eventClazz, Consumer<T> consumer) {
        attachRegistry(eventClazz, consumer);
    }

    @Override
    public void call(@NotNull Event event) {
        super.call(event);
        subscribePool.callGlobal(event);
        log.info("called event: {}", event.getClass());
    }
}
