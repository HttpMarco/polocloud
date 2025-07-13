package dev.httpmarco.polocloud.sdk.java.events.definitions

import dev.httpmarco.polocloud.sdk.java.events.Event
import dev.httpmarco.polocloud.sdk.java.services.Service

class ServiceShutdownEvent(val service: Service) : Event {
}