package dev.httpmarco.polocloud.suite.events;

import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.suite.PolocloudSuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EventSubscribePool {

    private final Map<ClusterService, List<String>> subscribedEvents = new HashMap<>();

    public boolean subscribe(String serviceName, String event) {
        var service = PolocloudSuite.instance().serviceProvider().find(serviceName);

        if (service == null) {
            return false;
        }

        var bindings = subscribedEvents.getOrDefault(service, new ArrayList<>());
        bindings.add(event);
        this.subscribedEvents.put(service, bindings);
        return true;
    }
}
