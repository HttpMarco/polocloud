package dev.httpmarco.polocloud.agent.runtime.local.tracking

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.runtime.local.LocalRuntime
import dev.httpmarco.polocloud.agent.runtime.local.LocalService
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.LinkedList

class LocalServiceLogTrack(private val service: LocalService) : LocalTrack() {

    val cachedLogs = LinkedList<String>();

    override fun start() {
        this.thread = Thread.startVirtualThread {
            service.process?.inputStream?.let { inputStream ->
                BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8)).use { reader ->
                    reader.lines().forEach { line ->
                        cachedLogs.add(line)

                        // print the line to the console directly if the screen used this service
                        if (Agent.instance.runtime is LocalRuntime) {
                            val runtime = Agent.instance.runtime
                            val screenService = runtime.terminal.screenService

                            if (screenService.isServiceRecoding(service)) {
                               runtime.terminal.screenService.terminal.display(line)
                            }
                        }
                    }
                }
            }
        }
    }
}