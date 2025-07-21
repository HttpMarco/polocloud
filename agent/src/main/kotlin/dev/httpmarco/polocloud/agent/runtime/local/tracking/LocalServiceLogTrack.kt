package dev.httpmarco.polocloud.agent.runtime.local.tracking

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.runtime.local.LocalRuntime
import dev.httpmarco.polocloud.agent.runtime.local.LocalService
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.LinkedList

class LocalServiceLogTrack(private val service: LocalService) : LocalTrack() {

    val cachedLogs = LinkedList<String>()

    override fun start() {
        this.thread = Thread.startVirtualThread {
            val input = service.process?.inputStream ?: return@startVirtualThread
            BufferedReader(InputStreamReader(input, StandardCharsets.UTF_8)).use { reader ->
                reader.lines().forEach { line ->
                    cachedLogs += line

                    val runtime = Agent.instance.runtime
                    if (runtime !is LocalRuntime) return@forEach

                    val screenService = runtime.terminal.screenService
                    if (!screenService.isServiceRecoding(service)) return@forEach

                    screenService.terminal.display(line)
                }
            }
        }
    }
}