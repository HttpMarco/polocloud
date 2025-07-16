package dev.httpmarco.polocloud.agent.runtime.local.tracking

import dev.httpmarco.polocloud.agent.runtime.local.LocalService
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.LinkedList

class LocalServiceLogTrack(private val service: LocalService) {

    val cachedLogs = LinkedList<String>();

    private lateinit var thread: Thread;

    fun start() {
        this.thread = Thread.startVirtualThread {
            service.process?.inputStream?.let { inputStream ->
                BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8)).use { reader ->
                    reader.lines().forEach { line -> cachedLogs.add(line) }
                }
            }
        }
    }

    fun close() {
        this.thread.interrupt()
    }
}