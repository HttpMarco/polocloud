package dev.httpmarco.polocloud.bridge.api

import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.PolocloudShared
import dev.httpmarco.polocloud.shared.events.Event
import dev.httpmarco.polocloud.shared.events.definitions.ServiceOnlineEvent
import dev.httpmarco.polocloud.shared.events.definitions.ServiceShutdownEvent
import dev.httpmarco.polocloud.shared.service.Service
import dev.httpmarco.polocloud.v1.GroupType
import dev.httpmarco.polocloud.v1.services.ServiceState

abstract class BridgeInstance<T> {

    abstract fun generateInfo(service: Service): T

    abstract fun registerService(identifier: T, fallback: Boolean = false)

    abstract fun unregisterService(identifier: T)

    abstract fun findInfo(name: String): T?

    private lateinit var polocloud: PolocloudShared

    fun initialize(polocloud: PolocloudShared) {
        this.polocloud = polocloud
        polocloud.serviceProvider().findByType(GroupType.SERVER).forEach {
            if(it.state !== ServiceState.ONLINE) {
                return@forEach
            }
            registerService(generateInfo(it), isFallback(it))
        }


        polocloud.eventProvider().subscribe(ServiceOnlineEvent::class.java) { event ->
            val service = event.service
            if (service.type == GroupType.SERVER) {
                registerService(generateInfo(service), isFallback(service))
            }
        }

        polocloud.eventProvider().subscribe(ServiceShutdownEvent::class.java) { event ->
            findInfo(event.service.name())?.let { info ->
                unregisterService(info)
            }!!
        }
    }

    fun initialize() {
        this.initialize(Polocloud.instance())
    }

    fun updatePolocloudPlayer(event: Event) {
        polocloud.eventProvider().call(event)
    }

    private fun isFallback(service: Service): Boolean {
        return service.properties["fallback"]?.equals("true", ignoreCase = true) == true
    }
}