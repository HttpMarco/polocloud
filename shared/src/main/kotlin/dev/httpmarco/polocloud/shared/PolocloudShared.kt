package dev.httpmarco.polocloud.shared

import dev.httpmarco.polocloud.shared.events.SharedEventProvider
import dev.httpmarco.polocloud.shared.groups.SharedGroupProvider
import dev.httpmarco.polocloud.shared.player.SharedPlayerProvider
import dev.httpmarco.polocloud.shared.service.SharedServiceProvider

lateinit var polocloudShared: PolocloudShared

abstract class PolocloudShared {

    abstract fun eventProvider(): SharedEventProvider

    abstract fun serviceProvider(): SharedServiceProvider<*>

    abstract fun groupProvider(): SharedGroupProvider<*>

    abstract fun playerProvider(): SharedPlayerProvider<*>

    init {
        polocloudShared = this
    }
}