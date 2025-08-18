package dev.httpmarco.polocloud.shared

import dev.httpmarco.polocloud.shared.events.SharedEventProvider
import dev.httpmarco.polocloud.shared.groups.SharedGroupProvider
import dev.httpmarco.polocloud.shared.logging.Logger
import dev.httpmarco.polocloud.shared.player.SharedPlayerProvider
import dev.httpmarco.polocloud.shared.service.SharedServiceProvider
import dev.httpmarco.polocloud.shared.information.SharedCloudInformationProvider
import dev.httpmarco.polocloud.shared.platform.SharedPlatformProvider

lateinit var polocloudShared: PolocloudShared

abstract class PolocloudShared {

    abstract fun eventProvider(): SharedEventProvider

    abstract fun serviceProvider(): SharedServiceProvider<*>

    abstract fun groupProvider(): SharedGroupProvider<*>

    abstract fun playerProvider(): SharedPlayerProvider<*>

    abstract fun cloudInformationProvider(): SharedCloudInformationProvider<*>

    abstract fun logger(): Logger

    /**
     * Specifies if lateinit variable <c>polocloudShared</c> should be set
     */
    abstract val setShared: Boolean

    abstract fun platformProvider(): SharedPlatformProvider<*>

    init {
        if (setShared) {
            polocloudShared = this
        }
    }
}