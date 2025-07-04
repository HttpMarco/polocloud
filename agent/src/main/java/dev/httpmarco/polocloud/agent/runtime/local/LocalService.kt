package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.groups.Group
import dev.httpmarco.polocloud.agent.services.Service
import java.util.LinkedList
import kotlin.io.path.Path

class LocalService(group: Group) : Service(group) {

    val cachedLogs = LinkedList<String>();
    var process: Process? = null
    val path = Path("temp/${name()}")

    fun startTracking() {
        TODO()
    }
}