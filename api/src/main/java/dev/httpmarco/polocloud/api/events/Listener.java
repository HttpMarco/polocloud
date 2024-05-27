package dev.httpmarco.polocloud.api.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public final class Listener<T extends Event> {
    private final Class<? extends T> event;
    private final EventRunnable<T> runnable;

}
