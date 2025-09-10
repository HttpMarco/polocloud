package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.runtime.abstract.AbstractRuntimeFactory
import dev.httpmarco.polocloud.shared.events.definitions.service.ServiceChangeStateEvent
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot
import dev.httpmarco.polocloud.v1.services.ServiceState
import java.util.concurrent.TimeUnit
import kotlin.io.path.*

class LocalRuntimeFactory(var localRuntime: LocalRuntime) : AbstractRuntimeFactory<LocalService>(LOCAL_FACTORY_PATH) {

    init {
        // if folder exists, delete all files inside
        if (LOCAL_FACTORY_PATH.exists()) {
            LOCAL_FACTORY_PATH.toFile().listFiles()?.forEach { it.deleteRecursively() }
        }
        // init factory path
        LOCAL_FACTORY_PATH.createDirectories()
    }

    // todo IMPLEMENT AGAIN
        /*
        val (correctRuntime, currentRuntime) = checkRuntimeVersion(service)
        if (!correctRuntime) {
            if (currentRuntime == null) {
                i18n.warn(
                    "agent.local-runtime.factory.boot.missing-runtime",
                    service.group.platform().language,
                    versionObject?.requiredRuntimeVersion
                )
            } else {
                i18n.warn(
                    "agent.local-runtime.factory.boot.wrong-runtime",
                    currentRuntime,
                    service.group.platform().language,
                    versionObject?.requiredRuntimeVersion
                )
            }
        }

         */

        /*
        while (Agent.runtime.serviceStorage().findAll()
                .count { it.state == ServiceState.STARTING } >= Agent.config.maxConcurrentServersStarts
            ||
            cpuUsage() > Agent.config.maxCPUPercentageToStart
        ) {
            Thread.sleep(1000)
        }

         */

    @OptIn(ExperimentalPathApi::class)
    override fun shutdownApplication(service: LocalService, shutdownCleanUp: Boolean): ServiceSnapshot {
        if (service.state == ServiceState.STOPPING || service.state == ServiceState.STOPPED) {
            return service.toSnapshot()
        }

        service.state = ServiceState.STOPPING
        val eventService = Agent.eventService

        i18n.info("agent.local-runtime.factory.shutdown", service.name())


        // first, we need to drop all subscriptions for this service
        // the service went down, so we don't need to send any events anymore
        eventService.dropServiceSubscriptions(service)
        // then we call the shutdown event -> for all other services
        eventService.call(ServiceChangeStateEvent(service))

        if (service.process != null) {
            try {
                val shutdownCommand = service.group.platform().shutdownCommand
                if (shutdownCommand.isNotEmpty() && shutdownCleanUp && service.executeCommand(shutdownCommand)) {
                    if (service.process!!.waitFor(5, TimeUnit.SECONDS)) {
                        service.process!!.exitValue()
                        service.state = ServiceState.STOPPED
                    }
                }
            } catch (_: Exception) {
                // ignore exceptions, we just want to stop the process
            }

            if (service.state != ServiceState.STOPPED) {

                service.process!!.toHandle().children().forEach { child ->
                    try {
                        child.destroy()
                    } catch (_: Exception) {
                        // ignore exceptions, we just want to stop the process}
                    }
                }

                service.process!!.toHandle().destroyForcibly()
                service.process!!.waitFor()
                service.process = null
                service.state = ServiceState.STOPPED
            }
        }

        if (localRuntime.terminal.screenService.isServiceRecoding(service)) {
            localRuntime.terminal.screenService.stopCurrentRecording()
        }
        service.stopTracking()

        // windows need some time to destroy the process
        if (!Thread.currentThread().isVirtual && shutdownCleanUp) {
            Thread.sleep(200) // wait for a process to be destroyed
        }

        if (!service.isStatic()) {
            service.path.deleteRecursively()
        }

        service.state = ServiceState.STOPPED
        Agent.eventProvider().call(ServiceChangeStateEvent(service))
        Agent.runtime.serviceStorage().dropAbstractService(service)
        i18n.info(
            "agent.local-runtime.factory${if (service.isStatic()) ".static" else ""}.shutdown.successful",
            service.name()
        )

        return service.toSnapshot()
    }

    override fun generateInstance(group: AbstractGroup): LocalService {
        return LocalService(group)
    }

    fun shutdown() {
        cacheThreadPool.shutdown()
    }

    override fun runRuntimeBoot(service: LocalService) {
        val processBuilder = ProcessBuilder(languageSpecificBootArguments(service)).directory(service.path.toFile())
        processBuilder.environment().putAll(
            mapOf(
                Pair("agent_port", Agent.config.port.toString()),
                Pair("service-name", service.name())
            )
        )

        service.process = processBuilder.start()
        service.startTracking()
    }
}