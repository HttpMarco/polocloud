package dev.httpmarco.polocloud.shared

import dev.httpmarco.polocloud.shared.events.EventProvider

val polocloudShared: PolocloudShared = TODO()

abstract class PolocloudShared {

    abstract fun eventProvider(): EventProvider

}