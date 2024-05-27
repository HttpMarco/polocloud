package dev.httpmarco.polocloud.api.events;

public interface EventRunnable<T extends Event> {
    void run(T event);

    @SuppressWarnings("unchecked")
    default void runMapped(Event event) {
        this.run((T) event);
    }

}
