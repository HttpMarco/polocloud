package dev.httpmarco.polocloud.api.event;

import java.util.function.Consumer;

public interface EventProvider {

    EventFactory factory();

    <T extends Event> void listen(Class<T> eventClazz, Consumer<T> event);

}
