package dev.httpmarco.polocloud.suite.events.pool;

import dev.httpmarco.polocloud.api.event.Event;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.suite.services.ClusterLocalServiceImpl;
import dev.httpmarco.polocloud.suite.utils.GsonInstance;

import java.util.*;

public final class EventSubscribePool {

    private final Map<ClusterLocalServiceImpl, List<String>> subscribedEvents = new HashMap<>();

    public boolean subscribe(ClusterService service, String event) {
        if (service == null) {
            return false;
        }

        if (service instanceof ClusterLocalServiceImpl localService) {
            var bindings = subscribedEvents.getOrDefault(service, new ArrayList<>());
            bindings.add(event);
            this.subscribedEvents.put(localService, bindings);
            return true;
        }
        return false;
    }

    public void callGlobal(Event event) {
        subscribedEvents.forEach((localService, events) -> {
            for (String eventId : events) {
                if (eventId.equals(event.getClass().getName())) {

                    if(localService.channel() == null) {
                        continue;
                    }

                    localService.eventServiceBlockingStub().call(dev.httpmarco.polocloud.explanation.event.EventServiceOuterClass.Event.newBuilder()
                            .setEventClass(eventId)
                            .setContent(GsonInstance.DEFAULT.toJson(event))
                            .build());
                }
            }
        });
    }
}
