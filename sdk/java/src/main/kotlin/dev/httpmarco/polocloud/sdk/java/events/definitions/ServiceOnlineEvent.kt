package dev.httpmarco.polocloud.sdk.java.events.definitions

import dev.httpmarco.polocloud.sdk.java.events.Event
import dev.httpmarco.polocloud.sdk.java.services.Service

class ServiceOnlineEvent(val service: Service) : Event {
}