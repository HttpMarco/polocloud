package dev.httpmarco.polocloud.api.event;

import java.util.function.Consumer;

public interface EventProvider {

    <T extends Event> void subscribe(Class<T> eventClazz, Consumer<T> consumer);

    void call(Event event);

}
