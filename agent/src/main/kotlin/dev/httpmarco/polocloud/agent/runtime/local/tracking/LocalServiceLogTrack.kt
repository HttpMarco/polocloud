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
        val process = service.process ?: return

        fun handleStream(reader: BufferedReader) {
            reader.useLines { lines ->
                lines.forEach { line ->
                    cachedLogs += line

                    val runtime = Agent.instance.runtime
                    if (runtime !is LocalRuntime) return@forEach

                    val screenService = runtime.terminal.screenService
                    if (!screenService.isServiceRecoding(service)) return@forEach

                    screenService.terminal.display(line)
                }
            }
        }

        this.threads.add(Thread.startVirtualThread {
            val inputReader = BufferedReader(InputStreamReader(process.inputStream, StandardCharsets.UTF_8))
            handleStream(inputReader)
        })

        this.threads.add(Thread.startVirtualThread {
            val errorReader = BufferedReader(InputStreamReader(process.errorStream, StandardCharsets.UTF_8))
            handleStream(errorReader)
        })
    }
}