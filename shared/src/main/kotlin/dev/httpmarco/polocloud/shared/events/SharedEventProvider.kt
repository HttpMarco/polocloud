package dev.httpmarco.polocloud.shared.events

import com.google.gson.GsonBuilder
import dev.httpmarco.polocloud.shared.service.Service
import dev.httpmarco.polocloud.shared.service.ServiceSerializer
import kotlin.reflect.KClass

abstract class SharedEventProvider {

    protected val gsonSerilaizer =
        GsonBuilder().registerTypeHierarchyAdapter(Service::class.java, ServiceSerializer()).create()

    abstract fun call(event: Event)

    abstract fun <T : Event> subscribe(eventType: Class<T>, result: (T) -> Any)

    fun <T : Event> subscribe(eventType: KClass<T>, result: (T) -> Any) {
        subscribe(eventType.java, result)
    }
}