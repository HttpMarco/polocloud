package dev.httpmarco.polocloud.agent.runtime.local.tracking

abstract class LocalTrack {

    protected val threads = mutableListOf<Thread>()

    abstract fun start()

    fun close() {
        threads.forEach { it.interrupt() }
    }
}