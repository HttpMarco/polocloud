package dev.httpmarco.polocloud.agent.events

import com.google.gson.GsonBuilder
import dev.httpmarco.polocloud.agent.events.serializer.ServiceDefinitionSerializer
import dev.httpmarco.polocloud.agent.services.Service
import dev.httpmarco.polocloud.v1.proto.EventProviderOuterClass
import io.grpc.stub.StreamObserver

class EventService {

    private val events = HashMap<String, List<StreamObserver<EventProviderOuterClass.EventContext>>>()

    fun attach(event: String, observer: StreamObserver<EventProviderOuterClass.EventContext>) {
        events.computeIfAbsent(event) { mutableListOf() }.let {
            it as MutableList
            it.add(observer)
        }
    }

    fun call(event: Event) {
        if (!events.containsKey(event.javaClass.simpleName)) {
            return
        }

        events[event.javaClass.simpleName]?.forEach { observer ->
            val eventName = event.javaClass.simpleName
            observer.onNext(
                EventProviderOuterClass.EventContext.newBuilder().setEventName(eventName)
                    .setEventData(GsonBuilder().registerTypeHierarchyAdapter(Service::class.java, ServiceDefinitionSerializer()).registerTypeAdapter(Service::class.java, ServiceDefinitionSerializer()).create().toJson(event)).build()
            )
        }
    }

    fun registeredAmount(): Int {
        return events.values.sumOf { it.size }
    }
}