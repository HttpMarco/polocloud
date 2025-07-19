package dev.httpmarco.polocloud.agent

import dev.httpmarco.polocloud.agent.logging.Logger
import org.jline.jansi.AnsiConsole
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import kotlin.io.path.Path
import kotlin.system.exitProcess

private val SHUTDOWN_HOOK = "polocloud-shutdown-hook"
private var inShutdown = false

fun registerHook() {
    Runtime.getRuntime().addShutdownHook(Thread({
        exitPolocloud()
    }, SHUTDOWN_HOOK))
}

fun exitPolocloud(cleanShutdown: Boolean = true) {

    if (inShutdown) {
        return
    }

    inShutdown = true

    logger.info("Polocloud Agent is shutting down&8...")


    try {
        Agent.instance.runtime.serviceStorage().items().forEach {
            it.shutdown(cleanShutdown)
        }

        Agent.instance.close()

        logger.info("Cleanup temp files&8...")
        Path("temp").toFile().deleteRecursively()
        logger.info("Successfully cleaned up temp files&8.")

        AnsiConsole.systemUninstall()
    } catch (e: Exception) {
        logger.throwable(e)
    }

    logger.info("Polocloud Agent is shutting down&8...")

    if (Thread.currentThread().name != SHUTDOWN_HOOK) {
        exitProcess(0)
    }
}

fun shutdownProcess(): Boolean {
    return inShutdown
}