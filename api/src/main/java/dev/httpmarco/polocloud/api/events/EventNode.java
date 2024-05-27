package dev.httpmarco.polocloud.api.events;

public interface EventNode {

    <T extends Event> void addListener(Class<T> event, EventRunnable<T> runnable);

    void call(Event event);

}
