package dev.httpmarco.polocloud.api.event;

import dev.httpmarco.polocloud.api.Named;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Getter
@Accessors(fluent = true)
public final class EventSubscribePool implements Named {

    private final String name;
    private final Map<String, EventActor> actors = new HashMap<>();

    public EventSubscribePool(String name) {
        EventPoolRegister.applyPool(this);

        this.name = name;
    }

    public void subscribe(String eventClass, EventActor eventActor) {
        this.actors.put(eventClass, eventActor);
    }

    public boolean hasActor(@NotNull Event event) {
        return this.actors.containsKey(event.getClass().getName());
    }

    public void acceptActor(@NotNull Event event) {
        if(!actors.containsKey(event.getClass().getName())) {
            return;
        }
        this.actors.get(event.getClass().getName()).alert(event);
    }
}
