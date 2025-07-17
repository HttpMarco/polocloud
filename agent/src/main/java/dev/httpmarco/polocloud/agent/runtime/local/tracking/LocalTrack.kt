package dev.httpmarco.polocloud.agent.runtime.local.tracking

abstract class LocalTrack {

    protected var thread: Thread? = null

    abstract fun start()

    fun close() {
        this.thread?.interrupt()
    }
}