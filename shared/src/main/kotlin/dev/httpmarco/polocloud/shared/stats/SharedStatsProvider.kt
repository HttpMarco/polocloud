package dev.httpmarco.polocloud.shared.stats

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