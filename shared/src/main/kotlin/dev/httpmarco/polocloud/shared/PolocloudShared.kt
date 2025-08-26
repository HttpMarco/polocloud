package dev.httpmarco.polocloud.shared

import dev.httpmarco.polocloud.shared.events.SharedEventProvider
import dev.httpmarco.polocloud.shared.groups.SharedGroupProvider
import dev.httpmarco.polocloud.shared.logging.Logger
import dev.httpmarco.polocloud.shared.player.SharedPlayerProvider
import dev.httpmarco.polocloud.shared.service.SharedServiceProvider
import dev.httpmarco.polocloud.shared.information.SharedCloudInformationProvider
import dev.httpmarco.polocloud.shared.platform.SharedPlatformProvider
import dev.httpmarco.polocloud.shared.template.SharedTemplateProvider

lateinit var polocloudShared: PolocloudShared

// setShared is needed because without the flag its impossible
// to use more than one SDK instance in one process (e.g. off premise bridge)
abstract class PolocloudShared(setShared: Boolean) {

    abstract fun eventProvider(): SharedEventProvider

    abstract fun serviceProvider(): SharedServiceProvider<*>

    abstract fun groupProvider(): SharedGroupProvider<*>

    abstract fun playerProvider(): SharedPlayerProvider<*>

    abstract fun cloudInformationProvider(): SharedCloudInformationProvider<*>

    abstract fun logger(): Logger

    abstract fun platformProvider(): SharedPlatformProvider<*>

    abstract fun templateProvider(): SharedTemplateProvider<*>

    init {
        if (setShared) {
            polocloudShared = this
        }
    }
}