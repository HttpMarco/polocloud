package dev.httpmarco.polocloud.base.events;

import dev.httpmarco.polocloud.api.events.Event;
import dev.httpmarco.polocloud.api.events.EventNode;
import dev.httpmarco.polocloud.api.events.EventRunnable;
import dev.httpmarco.polocloud.api.events.Listener;
import dev.httpmarco.polocloud.api.packets.event.CloudEventRegitserPacket;
import dev.httpmarco.polocloud.base.CloudBase;

import java.util.ArrayList;
import java.util.List;

public class GlobalEventNode<T extends Event> implements EventNode<T> {

    private final List<GlobalEventNode<T>> children = new ArrayList<>();
    private final List<Listener<T>> listeners = new ArrayList<>();

    public GlobalEventNode() {
        CloudBase.instance().transmitter().listen(CloudEventRegitserPacket.class, (channelTransmit, cloudEventRegitserPacket) -> {



            System.out.println("register: " + cloudEventRegitserPacket.event());
        });
    }

    public void call(T event) {
        listeners.stream()
                .filter(listener -> listener.event().equals(event.getClass()))
                .forEach(listener -> listener.runnable().run(event));

        children.forEach(children -> children.call(event));
    }

    public void addListener(Class<? extends T> event, EventRunnable<T> runnable) {
        listeners.add(new Listener<>(event, runnable));
    }

    public GlobalEventNode<T> addChildren(GlobalEventNode<T> globalEventNode) {
        children.add(globalEventNode);
        return this;
    }

    public GlobalEventNode<T> removeChildren(GlobalEventNode<T> globalEventNode) {
        this.children.remove(globalEventNode);
        return this;
    }
}
