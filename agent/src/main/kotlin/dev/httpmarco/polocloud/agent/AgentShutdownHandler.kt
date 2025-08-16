package dev.httpmarco.polocloud.agent

import dev.httpmarco.polocloud.updater.Updater
import org.jline.jansi.AnsiConsole
import kotlin.system.exitProcess

private val SHUTDOWN_HOOK = "polocloud-shutdown-hook"
private var inShutdown = false

fun registerHook() {
    Runtime.getRuntime().addShutdownHook(Thread({
        exitPolocloud()
    }, SHUTDOWN_HOOK))
}

fun exitPolocloud(cleanShutdown: Boolean = true, shouldUpdate: Boolean = false) {

    if (inShutdown) {
        return
    }

    inShutdown = true

    i18n.info("agent.shutdown.starting")


    try {
        Agent.runtime.serviceStorage().findAll().forEach {
            it.shutdown(cleanShutdown)
        }

        Agent.moduleProvider.unloadModules()

        Agent.close()

        AnsiConsole.systemUninstall()
    } catch (e: Exception) {
        logger.throwable(e)
    }

    i18n.info("agent.shutdown.successful")

    if (shouldUpdate) {
        Updater.update()
    }

    if (Thread.currentThread().name != SHUTDOWN_HOOK) {
        exitProcess(0)
    }
}

fun shutdownProcess(): Boolean {
    return inShutdown
}