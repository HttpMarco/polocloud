package dev.httpmarco.polocloud.shared

import dev.httpmarco.polocloud.shared.events.SharedEventProvider
import dev.httpmarco.polocloud.shared.groups.SharedGroupProvider
import dev.httpmarco.polocloud.shared.logging.Logger
import dev.httpmarco.polocloud.shared.player.SharedPlayerProvider
import dev.httpmarco.polocloud.shared.service.SharedServiceProvider

lateinit var polocloudShared: PolocloudShared

abstract class PolocloudShared {

    abstract fun eventProvider(): SharedEventProvider

    abstract fun serviceProvider(): SharedServiceProvider<*>

    abstract fun groupProvider(): SharedGroupProvider<*>

    abstract fun playerProvider(): SharedPlayerProvider<*>

    abstract fun logger(): Logger

    /**
     * Specifies if lateinit variable <c>polocloudShared</c> should be set
     */
    abstract val setShared: Boolean

    init {
        if (setShared) {
            polocloudShared = this
        }
    }
}