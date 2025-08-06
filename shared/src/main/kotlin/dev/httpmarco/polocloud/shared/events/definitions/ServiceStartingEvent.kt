package dev.httpmarco.polocloud.shared.events.definitions

import dev.httpmarco.polocloud.shared.events.Event
import dev.httpmarco.polocloud.shared.service.Service

class ServiceStartingEvent(val service: Service) : Event {
}