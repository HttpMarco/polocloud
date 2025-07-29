package dev.httpmarco.polocloud.agent.events

import com.google.gson.GsonBuilder
import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.events.serializer.ServiceDefinitionSerializer
import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.services.Service
import dev.httpmarco.polocloud.shared.events.Event
import dev.httpmarco.polocloud.shared.events.SharedEventProvider
import dev.httpmarco.polocloud.v1.proto.EventProviderOuterClass
import io.grpc.stub.StreamObserver
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.reflect.KClass

class EventService : SharedEventProvider {

    private val events = ConcurrentHashMap<String, List<EventSubscription>>()

    fun attach(event: String, serviceName: String, observer: StreamObserver<EventProviderOuterClass.EventContext>) {

        val service = Agent.runtime.serviceStorage().findService(serviceName)

        if (service == null) {
            i18n.warn("agent.events.service.not-found", serviceName, event)
            observer.onCompleted()
            return
        }

        events.computeIfAbsent(event) { CopyOnWriteArrayList() }.let {
            it as MutableList
            it.add(EventSubscription(service, observer))
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
            val eventName = event.javaClass.simpleName

            val context = GsonBuilder().registerTypeHierarchyAdapter(
                Service::class.java,
                ServiceDefinitionSerializer()
            ).registerTypeAdapter(Service::class.java, ServiceDefinitionSerializer()).create().toJson(event)

            println(context)

            it.sub.onNext(EventProviderOuterClass.EventContext.newBuilder().setEventName(eventName).setEventData(context).build())
        }
    }

    override fun <T : Event> subscribe(
        eventType: KClass<T>,
        result: (T) -> Any
    ) {
        TODO("Not yet implemented")
    }
}