package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.RuntimeFactory
import dev.httpmarco.polocloud.agent.services.AbstractService
import dev.httpmarco.polocloud.agent.utils.JavaUtils
import dev.httpmarco.polocloud.common.os.cpuUsage
import dev.httpmarco.polocloud.common.os.currentOS
import dev.httpmarco.polocloud.common.version.polocloudVersion
import dev.httpmarco.polocloud.common.image.pngToBase64DataUrl
import dev.httpmarco.polocloud.platforms.Platform
import dev.httpmarco.polocloud.platforms.PlatformLanguage
import dev.httpmarco.polocloud.platforms.PlatformParameters
import dev.httpmarco.polocloud.shared.events.definitions.ServiceShutdownEvent
import dev.httpmarco.polocloud.shared.events.definitions.ServiceStartingEvent
import dev.httpmarco.polocloud.shared.events.definitions.ServiceStoppingEvent
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot
import dev.httpmarco.polocloud.v1.services.ServiceState
import org.yaml.snakeyaml.util.Tuple
import java.nio.file.Files
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.io.path.*

class LocalRuntimeFactory(var localRuntime: LocalRuntime) : RuntimeFactory<LocalService> {

    val cacheThreadPool: ExecutorService by lazy { Executors.newFixedThreadPool(Agent.config.maxCachingProcesses) }
    val runningCacheProcesses: MutableList<Tuple<String, String>> by lazy {
        Collections.synchronizedList(
            mutableListOf()
        )
    }
    val waitingServices: MutableList<LocalService> by lazy { Collections.synchronizedList(mutableListOf()) }

    init {
        // if folder exists, delete all files inside
        if (LOCAL_FACTORY_PATH.exists()) {
            LOCAL_FACTORY_PATH.toFile().listFiles()?.forEach { it.deleteRecursively() }
        }
        // init factory path
        LOCAL_FACTORY_PATH.createDirectories()
    }

    override fun bootApplication(service: LocalService) {
        if (service.state != ServiceState.PREPARING) {
            i18n.error("agent.local-runtime.factory.boot.error", service.name(), service.state)
            return
        }

        val platform = service.group.platform()
        val version = service.group.platform.version

        val serverIcon = this.javaClass.classLoader.getResource("server-icon.png")!!

        val environment = PlatformParameters(
            platform.version(version)
        )
        environment.addParameter("hostname", service.hostname)
        environment.addParameter("port", service.port)
        environment.addParameter("server_icon", pngToBase64DataUrl(serverIcon.openStream()))
        environment.addParameter("agent_port", Agent.config.port)
        environment.addParameter("service-name", service.name())
        environment.addParameter("velocityProxyToken", Agent.securityProvider.proxySecureToken)
        environment.addParameter("file_suffix", platform.language.suffix())
        environment.addParameter("filename", service.group.applicationPlatformFile().name)

        // find a better way here
        val velocityPlatforms = listOf("velocity", "gate")
        environment.addParameter(
            "velocity_use",
            Agent.runtime.groupStorage().findAll().stream().anyMatch { velocityPlatforms.contains(it.platform().name) })
        environment.addParameter("version", polocloudVersion())

        //loading cache before starting service
        val cacheIsRunning = runningCacheProcesses.any { platform.name == it._1() && version == it._2() }
        if (!platform.cacheExists(version) || cacheIsRunning) {
            waitingServices.add(service)

            if (!cacheIsRunning) {
                this.handleMissingCache(platform, version, environment)
            }

            return
        }

        while (Agent.runtime.serviceStorage().findAll()
                .count { it.state == ServiceState.STARTING } >= Agent.config.maxConcurrentServersStarts
            ||
            cpuUsage() > Agent.config.maxCPUPercentageToStart
        ) {
            Thread.sleep(1000)
        }

        i18n.info("agent.local-runtime.factory.boot.up", service.name())

        service.state = ServiceState.STARTING
        Agent.eventService.call(ServiceStartingEvent(service))

        service.path.createDirectories()

        // copy all templates to the service path
        Agent.runtime.templates().bindTemplate(service)

        // copy the platform files to the service path and setup service
        platform.prepare(service.path, service.group.platform.version, environment)


        val serverIconPath = service.path.resolve("server-icon.png")
        // copy server-icon if not exists
        if (Files.notExists(serverIconPath)) {
            Files.copy(serverIcon.openStream(), serverIconPath)
        }

        // basically current only the java command is supported yet
        val commands = getLanguageSpecificCommands(platform, service)

        val processBuilder = ProcessBuilder(commands).directory(service.path.toFile())
        processBuilder.environment().putAll(
            mapOf(
                Pair("agent_port", Agent.config.port.toString()),
                Pair("service-name", service.name())
            )
        )

        service.process = processBuilder.start()
        service.startTracking()
    }

    @OptIn(ExperimentalPathApi::class)
    override fun shutdownApplication(service: LocalService, shutdownCleanUp: Boolean): ServiceSnapshot {
        if (service.state == ServiceState.STOPPING || service.state == ServiceState.STOPPED) {
            return service.toSnapshot()
        }

        service.state = ServiceState.STOPPING
        val eventService = Agent.eventService
        eventService.call(ServiceStoppingEvent(service))

        i18n.info("agent.local-runtime.factory.shutdown", service.name())


        // first, we need to drop all subscriptions for this service
        // the service went down, so we don't need to send any events anymore
        eventService.dropServiceSubscriptions(service)
        // then we call the shutdown event -> for all other services
        eventService.call(ServiceShutdownEvent(service))

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
        Agent.runtime.serviceStorage().dropAbstractService(service)
        i18n.info(
            "agent.local-runtime.factory${if (service.isStatic()) ".static" else ""}.shutdown.successful",
            service.name()
        )

        return service.toSnapshot()
    }

    fun shutdown() {
        cacheThreadPool.shutdown()
    }

    private fun getLanguageSpecificCommands(platform: Platform, abstractService: AbstractService): ArrayList<String> {
        val commands = ArrayList<String>()

        when (platform.language) {
            PlatformLanguage.JAVA -> {

                val javaPath = abstractService.group.properties["javaPath"]?.takeIf {
                    it.isString && JavaUtils().isValidJavaPath(it.asString)
                } ?: System.getProperty("java.home")

                commands.add("${javaPath}/bin/java")
                commands.addAll(
                    listOf(
                        "-Dterminal.jline=false",
                        "-Dfile.encoding=UTF-8",
                        "-Xms" + abstractService.minMemory + "M",
                        "-Xmx" + abstractService.maxMemory + "M",
                        "-jar",
                        abstractService.group.applicationPlatformFile().name
                    )
                )
                commands.addAll(platform.arguments)
            }

            PlatformLanguage.GO, PlatformLanguage.RUST -> {
                commands.addAll(currentOS.executableCurrentDirectoryCommand(abstractService.group.applicationPlatformFile().name))
            }
        }
        return commands
    }

    private fun handleMissingCache(platform: Platform, version: String, environment: PlatformParameters) {
        val platformName = platform.name

        val processEntry = Tuple(platformName, version)
        runningCacheProcesses.add(processEntry)

        cacheThreadPool.execute {
            i18n.info("agent.local-runtime.factory.boot.platform.prepare", version, platformName)
            platform.cachePrepare(version, environment)
            runningCacheProcesses.remove(processEntry)

            val servicesToBoot =
                waitingServices.filter { it.group.platform.name == platform.name && it.group.platform.version == version }
            servicesToBoot.forEach {
                this.bootApplication(it)
            }
            waitingServices.removeAll(servicesToBoot)
        }
    }
}