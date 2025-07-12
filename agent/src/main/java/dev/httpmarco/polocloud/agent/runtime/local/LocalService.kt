package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.groups.Group
import dev.httpmarco.polocloud.agent.services.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.LinkedList
import kotlin.io.path.Path

class LocalService(group: Group, id: Int) : Service(group, id) {

    val cachedLogs = LinkedList<String>();
    var process: Process? = null
    val path = Path("temp/${name()}")

    fun startTracking() {
        Thread.startVirtualThread {
            process?.inputStream?.let { inputStream ->
                BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8)).use { reader ->
                    reader.lines().forEach { line -> cachedLogs.add(line) }
                }
            }
        }
    }
}