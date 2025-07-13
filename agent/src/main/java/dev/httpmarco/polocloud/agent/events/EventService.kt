package dev.httpmarco.polocloud.agent.events

import com.google.gson.GsonBuilder
import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.events.serializer.ServiceDefinitionSerializer
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.services.Service
import dev.httpmarco.polocloud.v1.proto.EventProviderOuterClass
import io.grpc.stub.StreamObserver

class EventService {

    private val events = HashMap<String, List<EventSubscription>>()

    fun attach(event: String, serviceName: String, observer: StreamObserver<EventProviderOuterClass.EventContext>) {

        val service = Agent.instance.runtime.serviceStorage().findService(serviceName)

        if (service == null) {
            logger.warn("Service $service not found for event subscription $event. Ignoring subscription.")
            observer.onCompleted()
            return
        }

        events.computeIfAbsent(event) { mutableListOf() }.let {
            it as MutableList
            it.add(EventSubscription(service, observer))
        }
    }

    fun call(event: Event) {
        if (!events.containsKey(event.javaClass.simpleName)) {
            return
        }

        events[event.javaClass.simpleName]?.forEach {
            val eventName = event.javaClass.simpleName
            it.sub.onNext(
                EventProviderOuterClass.EventContext.newBuilder().setEventName(eventName)
                    .setEventData(
                        GsonBuilder().registerTypeHierarchyAdapter(
                            Service::class.java,
                            ServiceDefinitionSerializer()
                        ).registerTypeAdapter(Service::class.java, ServiceDefinitionSerializer()).create().toJson(event)
                    ).build()
            )
        }
    }

    fun dropServiceSubscriptions(service: Service) {
        events.forEach { (event, subscriptions) ->
            events[event] = subscriptions.filterNot { it.service == service }
        }
    }

    fun registeredAmount(): Int {
        return events.values.sumOf { it.size }
    }
}