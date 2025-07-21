package dev.httpmarco.polocloud.bridge.api

import dev.httpmarco.polocloud.shared.events.definitions.ServiceOnlineEvent
import dev.httpmarco.polocloud.shared.events.definitions.ServiceShutdownEvent
import dev.httpmarco.polocloud.shared.polocloudShared

abstract class BridgeInstance<T> {

    abstract fun generateInfo(name: String, hostname: String, port: Int): T

    abstract fun registerService(identifier: T)

    abstract fun unregisterService(identifier: T)

    abstract fun findInfo(name: String): T?

    fun initialize() {
        polocloudShared.eventProvider().subscribe(ServiceOnlineEvent::class) {
            val service = it.service

            registerService(generateInfo(service.name(), service.hostname, service.port))
        }

        polocloudShared.eventProvider().subscribe(ServiceShutdownEvent::class) {
            val info = findInfo(it.service.name())
            if (info != null) {
                unregisterService(info)
            }
        }
    }
}