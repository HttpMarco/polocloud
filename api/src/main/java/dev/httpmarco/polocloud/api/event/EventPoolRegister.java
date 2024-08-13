package dev.httpmarco.polocloud.api.event;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
@Getter
@Accessors(fluent = true)
public class EventPoolRegister {

    private final List<EventSubscribePool> pools = new ArrayList<>();

    public void applyPool(EventSubscribePool eventSubscribePool) {
        pools.add(eventSubscribePool);
    }

    public void remove(EventSubscribePool pool) {
        pools.remove(pool);
    }

    public List<EventSubscribePool> pools() {
        return pools;
    }
}
