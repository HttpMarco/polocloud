package dev.httpmarco.polocloud.shared.events.definitions.service

import dev.httpmarco.polocloud.shared.events.Event
import dev.httpmarco.polocloud.shared.service.Service

class ServiceLogEvent(val service: Service, val line: String) : Event {
}