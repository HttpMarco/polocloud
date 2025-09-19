package dev.httpmarco.polocloud.shared.events.definitions.service

import dev.httpmarco.polocloud.shared.events.Event
import dev.httpmarco.polocloud.shared.service.Service

class ServiceChangeStateEvent(val service: Service) : Event {
}