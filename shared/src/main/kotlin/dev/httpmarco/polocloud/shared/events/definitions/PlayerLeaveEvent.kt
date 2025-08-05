package dev.httpmarco.polocloud.shared.events.definitions

import dev.httpmarco.polocloud.shared.events.Event
import dev.httpmarco.polocloud.shared.player.PolocloudPlayer

class PlayerLeaveEvent(val player: PolocloudPlayer) : Event {
}