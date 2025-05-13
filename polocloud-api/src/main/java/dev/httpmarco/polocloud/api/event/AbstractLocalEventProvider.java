package dev.httpmarco.polocloud.api.event;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Getter
@Accessors(fluent = true)
public abstract class AbstractLocalEventProvider implements EventProvider {

    /**
     * A map that holds subscribers for specific event types.
     * <p>
     * The keys represent classes of events extending the {@link Event} interface,
     * and the values are lists of consumers tied to their corresponding event type.
     * The map is used to manage and dispatch local event subscriptions.
     */
    private final Map<Class<? extends Event>, List<Consumer<Event>>> registry = new HashMap<>();

    /**
     * Attaches a consumer to the event registry for a specific event type.
     * This allows the consumer to be notified when an event of the specified type is triggered.
     *
     * @param eventClazz the class of the event to which the consumer should be attached
     * @param consumer   the consumer that will process events of the specified type
     */
    @SuppressWarnings("unchecked")
    public void attachRegistry(Class<? extends Event> eventClazz, Consumer<? extends Event> consumer) {
        // Get the current registry for the event type.
        // Multiple consumers can be subscribed to a single event type.
        var current = registry.getOrDefault(eventClazz, new ArrayList<>());
        current.add((Consumer<Event>) consumer);
        // Update the registry with the new consumer.
        this.registry.put(eventClazz, current);
    }

    @Override
    public void call(@NotNull Event event) {
        if (!registry.containsKey(event.getClass())) {
            return;
        }
        this.registry.get(event.getClass()).forEach(consumer -> consumer.accept(event));
    }
}
