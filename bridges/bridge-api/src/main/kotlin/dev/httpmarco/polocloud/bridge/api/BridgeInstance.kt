package dev.httpmarco.polocloud.bridge.api

import dev.httpmarco.polocloud.shared.events.definitions.ServiceOnlineEvent
import dev.httpmarco.polocloud.shared.events.definitions.ServiceShutdownEvent
import dev.httpmarco.polocloud.shared.polocloudShared
import dev.httpmarco.polocloud.shared.service.Service
import dev.httpmarco.polocloud.v1.GroupType

abstract class BridgeInstance<T> {

    abstract fun generateInfo(name: String, hostname: String, port: Int): T

    abstract fun registerService(identifier: T, fallback: Boolean = false)

    abstract fun unregisterService(identifier: T)

    abstract fun findInfo(name: String): T?

    init {
        // it is bad, but if sdk ist present, we can use it
        Class.forName("dev.httpmarco.polocloud.sdk.java.Polocloud")
    }

    fun initialize() {
        polocloudShared.serviceProvider().findByType(GroupType.SERVER).forEach {
            registerService(generateInfo(it.name(), it.hostname, it.port), isFallback(it))
        }

        polocloudShared.eventProvider().subscribe(ServiceOnlineEvent::class) {
           val service = it.service
            if (service.type == GroupType.SERVER) {
                registerService(generateInfo(service.name(), service.hostname, service.port), isFallback(service))
            }
        }

        polocloudShared.eventProvider().subscribe(ServiceShutdownEvent::class) {
            findInfo(it.service.name())?.let { info ->
                unregisterService(info)
            }!!
        }
    }

    private fun isFallback(service: Service): Boolean {
        return service.properties["fallback"]?.equals("true", ignoreCase = true) == true
    }
}