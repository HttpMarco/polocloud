package dev.httpmarco.polocloud.shared.events.definitions.service

import dev.httpmarco.polocloud.shared.events.Event
import dev.httpmarco.polocloud.shared.service.Service

class ServiceChangePlayerCountEvent(val service: Service): Event {
}