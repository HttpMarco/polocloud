package dev.httpmarco.polocloud.agent.runtime

import dev.httpmarco.polocloud.agent.services.Service

/**
 * Interface for extending the runtime capabilities of a service.
 * Provides methods to execute commands, read logs, and get memory usage.
 *
 * @param S The type of service this expander works with.
 */
interface RuntimeExpender<out S : Service> {

    /**
     * Executes a command on the service.
     * Returns true if the command was successfully executed,
     * false if the service is not running or the command could not be executed.
     */
    fun executeCommand(service: @UnsafeVariance S, command: String): Boolean

    /**
     * Reads the last [lines] lines of logs from the service.
     * If [lines] is -1, reads all logs.
     * Returns an empty list if the service is not running or has no logs.
     */
    fun readLogs(service: @UnsafeVariance S, lines: Int = 100): List<String>

}