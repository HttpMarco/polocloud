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

    /**
     * Aggregated by minutes (avg cpu/ram per minute).
     */
    fun findMinutes(from: Long, to: Long): List<AggregateCloudInformation>

    /**
     * Aggregated by hours (avg cpu/ram per hour).
     */
    fun findHours(from: Long, to: Long): List<AggregateCloudInformation>

    /**
     * Aggregated by days (avg cpu/ram per day).
     */
    fun findDays(from: Long, to: Long): List<AggregateCloudInformation>

    /**
     * Average CPU/RAM usage across a range.
     */
    fun findAverage(from: Long, to: Long): AggregateCloudInformation

    /**
     * Cleanup old entries older than `maxAgeMillis` (raw + aggregates).
     */
    fun cleanup(maxAgeMillis: Long)

}