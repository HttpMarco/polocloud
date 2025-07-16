package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.groups.Group
import dev.httpmarco.polocloud.agent.runtime.local.tracking.LocalServiceLogTrack
import dev.httpmarco.polocloud.agent.services.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.LinkedList
import kotlin.io.path.Path

class LocalService(group: Group, id: Int) : Service(group, id) {

    private val logTracker = LocalServiceLogTrack(this)

    var process: Process? = null
    val path = Path("temp/${name()}")

    fun startTracking() {
        this.logTracker.start()
    }

    fun stopTracking() {
        this.logTracker.close()
    }

    fun logs() : List<String> {
        return logTracker.cachedLogs
    }
}