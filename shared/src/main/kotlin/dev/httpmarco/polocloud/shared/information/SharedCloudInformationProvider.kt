package dev.httpmarco.polocloud.shared.information

/**
 * Interface for a shared service provider that allows interaction with services.
 */
interface SharedCloudInformationProvider<S : CloudInformation> {

    /**
     * Get cloud statistics.
     *
     * @return stats snapshot
     */
    fun get(): S

}