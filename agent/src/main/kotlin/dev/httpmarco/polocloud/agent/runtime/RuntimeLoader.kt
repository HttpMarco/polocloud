package dev.httpmarco.polocloud.agent.runtime

interface RuntimeLoader {

    /**
     * Check if the runtime is runnable.
     * This method should be overridden by the specific runtime implementations
     */
    fun runnable(): Boolean

    fun instance(): Runtime

}