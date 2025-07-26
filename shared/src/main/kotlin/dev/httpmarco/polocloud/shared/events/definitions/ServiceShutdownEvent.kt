package dev.httpmarco.polocloud.shared.events.definitions

import dev.httpmarco.polocloud.shared.events.Event
import dev.httpmarco.polocloud.shared.service.Service

class ServiceShutdownEvent(val service: Service) : Event {
}