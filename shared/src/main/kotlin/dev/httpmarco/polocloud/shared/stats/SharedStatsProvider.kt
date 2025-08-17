package dev.httpmarco.polocloud.shared.stats

import dev.httpmarco.polocloud.shared.groups.Group
import dev.httpmarco.polocloud.shared.service.Service
import dev.httpmarco.polocloud.shared.service.SharedBootConfiguration
import dev.httpmarco.polocloud.v1.GroupType
import dev.httpmarco.polocloud.v1.groups.StatsSnapshot
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot
import java.util.concurrent.CompletableFuture

/**
 * Interface for a shared service provider that allows interaction with services.
 */
interface SharedStatsProvider<S : Stats> {

    /**
     * Get cloud statistics.
     *
     * @return stats snapshot
     */
    fun get(): S

}