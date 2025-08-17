package dev.httpmarco.polocloud.shared.information

/**
 * Interface for a shared service provider that allows interaction with services.
 */
interface SharedCloudInformationProvider<S : CloudInformation> {

    /**
     * Get current cloud statistics.
     *
     * @return stats snapshot
     */
    fun find(): S

    /**
     * Retrieves a list of CloudInformation within the specified range.
     *
     * @param from the starting point of the range (inclusive).
     * @param to the ending point of the range (inclusive).
     * @return a list of CloudInformation within the specified range.
     */
    fun find(from: Long, to: Long): List<S>

    /**
     * Retrieves a complete list of all available CloudInformation instances.
     *
     * @return a list containing all CloudInformation instances.
     */
    fun findAll(): List<S>

}