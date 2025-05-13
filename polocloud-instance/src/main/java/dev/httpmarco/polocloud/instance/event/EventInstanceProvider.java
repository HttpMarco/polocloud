package dev.httpmarco.polocloud.instance.event;

import dev.httpmarco.polocloud.api.PolocloudEnvironment;
import dev.httpmarco.polocloud.api.event.AbstractLocalEventProvider;
import dev.httpmarco.polocloud.api.event.Event;
import dev.httpmarco.polocloud.api.event.EventProvider;
import dev.httpmarco.polocloud.explanation.event.EventServiceGrpc;
import dev.httpmarco.polocloud.explanation.event.EventServiceOuterClass;
import io.grpc.Channel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class EventInstanceProvider extends AbstractLocalEventProvider {

    private final EventServiceGrpc.EventServiceBlockingStub eventServiceBlockingStub;

    public EventInstanceProvider(Channel channel) {
        this.eventServiceBlockingStub = EventServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public <T extends Event> void subscribe(@NotNull Class<T> eventClazz, Consumer<T> consumer) {
        var response = eventServiceBlockingStub.subscribe(EventServiceOuterClass.EventRegisterRequest.newBuilder()
                .setEventClass(eventClazz.getName())
                .setServiceId(System.getenv(PolocloudEnvironment.POLOCLOUD_SERVICE_ID.name()))
                .build());

        if (!response.getSuccess()) {
            // todo use logger
            System.err.println("Failed to subscribe to event: " + eventClazz.getName());
            return;
        }

        super.attachRegistry(eventClazz, consumer);
    }

    @Override
    public void call(@NotNull Event event) {
        super.call(event);

        //todo global
    }
}
