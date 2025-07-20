package dev.httpmarco.polocloud.agent.runtime.local.tracking

import dev.httpmarco.polocloud.agent.runtime.local.LocalService

class LocalOnlineTrack(private val localService: LocalService) : LocalTrack() {

    override fun start() {
        thread = Thread.startVirtualThread {
            try {
                localService.process?.waitFor()
            }catch (_: InterruptedException) {
                // we can ignore this exception; it means the process was interrupted
            }

            // When the process ends -> we can detect it here
            localService.shutdown()
        }
    }
}