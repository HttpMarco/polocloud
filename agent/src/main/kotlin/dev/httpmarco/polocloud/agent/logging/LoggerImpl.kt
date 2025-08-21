package dev.httpmarco.polocloud.agent.logging

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.local.LocalRuntime
import dev.httpmarco.polocloud.agent.runtime.local.terminal.LoggingColor
import dev.httpmarco.polocloud.agent.shutdownProcess
import dev.httpmarco.polocloud.shared.events.definitions.PolocloudLogEvent
import dev.httpmarco.polocloud.shared.logging.Logger
import java.time.LocalTime

class LoggerImpl : Logger {

    private val logBuffer = mutableListOf<String>()
    private var bufferingLogs = false
    private var debugMode = false

    override fun info(message: String) {
        log("INFO", "&f", message)
    }

    override fun warn(message: String) {
        log("WARN", "&e", message)
    }

    override fun error(message: String) {
        log("ERROR", "&c", message)
    }

    fun throwable(throwable: Throwable) {
        // Handle other exceptions that may occur during reading
        logger.error("An error occurred thread: ${throwable.message}")
        // for a better debugging experience, we print the stack trace
        throwable.stackTrace.forEach {
            logger.error("  at ${it.className}.${it.methodName}(${it.fileName}:${it.lineNumber})")
        }
    }

    override fun debug(message: String) {
        if (!debugMode) return
        log("DEBUG", "&f", message)
    }

    fun enableLogBuffering() {
        this.bufferingLogs = true
        this.logBuffer.clear()
    }

    fun flushLogs() {
        this.bufferingLogs = false
        this.logBuffer.forEach { outputLog(it) }
        this.logBuffer.clear()
    }

    private fun log(level: String, style: String, message: String) {
        val timestamp = LocalTime.now().withNano(0).format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"))
        val formatted = LoggingColor.translate("$timestamp &8| $style$level&8: &7$message")

        if (bufferingLogs) {
            this.logBuffer.add(formatted)
            return
        }

        outputLog(formatted)
    }

    private fun outputLog(message: String) {
        Agent.eventProvider().call(PolocloudLogEvent(message))

        if (Agent.runtime is LocalRuntime && !shutdownProcess()) {
            Agent.runtime.terminal.display(message)
            return
        }
        println(message)
    }
}