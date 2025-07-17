package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.RuntimeExpender
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter

class LocalRuntimeExpender : RuntimeExpender<LocalService> {

    override fun executeCommand(service: LocalService, command: String): Boolean {
        val process = service.process

        if (process == null || command.isBlank()) {
            logger.warn("Cannot execute command on service ${service.name()}: Process is null or command is blank")
            return false
        }

        if (!process.isAlive) {
            return false
        }

        try {
            val writer = BufferedWriter(OutputStreamWriter(process.outputStream))
            writer.write(command)
            writer.newLine()
            writer.flush()
            return true
        } catch (e: IOException) {
            logger.warn("Failed to write command to service ${service.name()}: ${e.message}")
            logger.throwable(e)
            return false
        }
    }

    override fun readLogs(service: LocalService, lines: Int): List<String> {
        if (lines == -1) {
            return service.logs()
        }

        return service.logs().takeLast(lines)
    }
}