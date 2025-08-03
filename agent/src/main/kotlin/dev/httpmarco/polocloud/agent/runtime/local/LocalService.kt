package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.runtime.local.tracking.LocalOnlineTrack
import dev.httpmarco.polocloud.agent.runtime.local.tracking.LocalServiceLogTrack
import dev.httpmarco.polocloud.agent.services.AbstractService
import oshi.software.os.OSProcess
import kotlin.io.path.Path


class LocalService(abstractGroup: AbstractGroup, id: Int, hostname: String = "127.0.0.1") : AbstractService(abstractGroup, id, hostname) {

    private val logTracker = LocalServiceLogTrack(this)
    private val onlineTrack = LocalOnlineTrack(this)

    var process: Process? = null
    val path = Path("temp/${name()}")

    var lastCpuSnapshot: OSProcess? = null
    var lastCpuUpdateTimeStamp = System.currentTimeMillis()

    fun startTracking() {
        this.onlineTrack.start()
        this.logTracker.start()
    }

    fun stopTracking() {
        this.logTracker.close()
        this.onlineTrack.close()
    }

    fun logs(): List<String> = logTracker.cachedLogs

    fun pid() = process?.pid()

}