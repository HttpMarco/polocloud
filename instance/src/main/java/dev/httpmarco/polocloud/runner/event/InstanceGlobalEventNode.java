package dev.httpmarco.polocloud.runner.event;

import dev.httpmarco.polocloud.api.events.Event;
import dev.httpmarco.polocloud.api.events.EventNode;
import dev.httpmarco.polocloud.api.events.EventRunnable;
import dev.httpmarco.polocloud.api.packets.event.CloudEventCallPacket;
import dev.httpmarco.polocloud.api.packets.event.CloudEventRegitserPacket;
import dev.httpmarco.polocloud.runner.Instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class InstanceGlobalEventNode implements EventNode {

    private final HashMap<Class<? extends Event>, List<EventRunnable<? extends Event>>> nodeListeners = new HashMap<>();

    public InstanceGlobalEventNode() {
        Instance.instance().client().transmitter().listen(CloudEventCallPacket.class, (channelTransmit, event) -> {
            if (nodeListeners.containsKey(event.event().getClass())) {
                for (EventRunnable<? extends Event> runnable : nodeListeners.get(event.event().getClass())) {
                    runnable.runMapped(event.event());
                }
            }
        });
    }

    @Override
    public <T extends Event> void addListener(Class<T> event, EventRunnable<T> runnable) {
        var currentListeners = nodeListeners.getOrDefault(event, new ArrayList<>());
        currentListeners.add(runnable);
        this.nodeListeners.put(event, currentListeners);

        Instance.instance().client().transmitter().sendPacket(new CloudEventRegitserPacket(Instance.SERVICE_ID, event.getName()));
    }

    @Override
    public void call(Event event) {

    }
}
