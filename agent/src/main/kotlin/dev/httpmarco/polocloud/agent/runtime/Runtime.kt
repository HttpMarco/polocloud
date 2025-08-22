package dev.httpmarco.polocloud.agent.runtime

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.runtime.docker.DockerRuntime
import dev.httpmarco.polocloud.agent.runtime.k8s.KubernetesRuntime
import dev.httpmarco.polocloud.agent.runtime.local.LocalRuntime
import dev.httpmarco.polocloud.agent.services.AbstractService

interface Runtime {

    companion object {
        /**
         * Creates a new instance of the runtime based on the current environment.
         * The order of preference is:
         *   1. Kubernetes
         *   2. Docker
         *   3. Local (fallback)
         *
         * @return the most suitable [Runtime] implementation for the current environment.
         */
        fun create(): Runtime {
            val runtime = listOf(
                KubernetesRuntime(),
                DockerRuntime(),
                LocalRuntime() // Fallback if others are not runnable
            ).firstOrNull { it.runnable() } ?: LocalRuntime()
            return runtime
        }
    }

    /**
     * Boot order
     * 1. Initialize() - called before boot
     * 2. boot() - called after initialize
     * 3. Runnable() - check if the runtime is runnable
     * ...
     * 4. shutdown - called to shut down the runtime
     */
    fun initialize() {
        // Default implementation does nothing.
        // This method can be overridden by specific runtime implementations
        // to perform any necessary initialization.
        // its called before the boot method

        // if nothing is done here, the boot method is called directly
        Agent.boot()
    }

    fun boot() {
        // Default implementation does nothing.
        // This method can be overridden by specific runtime implementations
        // to perform any necessary bootstrapping or initialization.
    }

    /**
     * Check if the runtime is runnable.
     * This method should be overridden by the specific runtime implementations
     */
    fun runnable(): Boolean

    /**
     * Returns the current storage for the runtime.
     * Only for all service related operations.
     */
    fun serviceStorage(): RuntimeServiceStorage<*>

    /**
     * Returns the current group storage for the runtime.
     * Only for all group related operations.
     */
    fun groupStorage(): RuntimeGroupStorage

    /**
     * Returns the current factory for the runtime.
     * This method should be overridden by the specific runtime implementations
     */
    fun factory(): RuntimeFactory<AbstractService>

    /**
     * Returns the expender for the runtime.
     * This method should be overridden by the specific runtime implementations
     */
    fun expender(): RuntimeExpender<AbstractService>

    /**
     * Returns the templates for the runtime.
     * This method should be overridden by the specific runtime implementations
     */
    fun templates(): RuntimeTemplates<AbstractService>

    /**
     * Returns the holder for the runtime configuration.
     */
    fun configHolder(): RuntimeConfigHolder

    /*
    Returns the start time of the cloud
     */
    fun started(): Long

    /**
     * Shuts down the runtime.
     * This method can be overridden by specific runtime implementations
     * to perform any necessary shutdown operations.
     */
    fun shutdown() {
        // Default implementation does nothing.
        // This method can be overridden by specific runtime implementations
        // to perform any necessary shutdown operations.
    }

    /**
     * Sends a command to the runtime.
     * This method can be overridden by specific runtime implementations
     * to send commands to the runtime.
     */
    fun sendCommand(command: String)
}