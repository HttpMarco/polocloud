package dev.httpmarco.polocloud.agent.events

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.shared.events.Event
import dev.httpmarco.polocloud.shared.events.SharedEventProvider
import dev.httpmarco.polocloud.shared.service.Service
import dev.httpmarco.polocloud.v1.proto.EventProviderOuterClass
import io.grpc.stub.ServerCallStreamObserver
import io.grpc.stub.StreamObserver
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class EventService : SharedEventProvider() {

    private val events = ConcurrentHashMap<String, List<EventSubscription>>()

    fun attach(event: String, serviceName: String, observer: StreamObserver<EventProviderOuterClass.EventContext>) {

        val service = Agent.runtime.serviceStorage().find(serviceName)

        if (service == null) {
            i18n.warn("agent.events.service.not-found", serviceName, event)
            observer.onCompleted()
            return
        }

        events.computeIfAbsent(event) { CopyOnWriteArrayList() }.let {
            it as MutableList
            it.add(EventSubscription(service,
                observer as ServerCallStreamObserver<EventProviderOuterClass.EventContext>
            ))
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

    override fun call(event: Event) {
        if (!events.containsKey(event.javaClass.simpleName)) {
            return
        }

        events[event.javaClass.simpleName]?.forEach {
            if(!it.sub.isCancelled) {
                it.sub.onNext(
                    EventProviderOuterClass.EventContext
                        .newBuilder()
                        .setEventName(event.javaClass.simpleName)
                        .setEventData(gsonSerilaizer.toJson(event))
                        .build()
                )
            }
        }
    }

    override fun <T : Event> subscribe(
        eventType: Class<T>,
        result: (T) -> Any
    ) {
        TODO("Not yet implemented")
    }
}