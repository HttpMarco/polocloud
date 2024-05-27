package dev.httpmarco.polocloud.api.events;

public interface EventNode<T extends Event> {

    void addListener(Class<? extends T> event, EventRunnable<T> runnable);

}
