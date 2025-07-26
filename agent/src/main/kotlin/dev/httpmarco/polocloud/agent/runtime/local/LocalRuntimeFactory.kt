package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.events.definitions.ServiceShutdownEvent
import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.runtime.RuntimeFactory
import dev.httpmarco.polocloud.agent.services.Service
import dev.httpmarco.polocloud.platforms.Platform
import dev.httpmarco.polocloud.platforms.PlatformLanguage
import dev.httpmarco.polocloud.platforms.PlatformType
import dev.httpmarco.polocloud.v1.ServiceState
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import kotlin.io.path.*

class LocalRuntimeFactory(var localRuntime: LocalRuntime) : RuntimeFactory<LocalService> {

    private val factoryPath = Path("temp")

    init {
        // if folder exists, delete all files inside
        if (factoryPath.exists()) {
            factoryPath.toFile().listFiles()?.forEach { it.deleteRecursively() }
        }
        // init factory path
        factoryPath.createDirectories()
    }

    override fun bootApplication(service: LocalService) {

        if (service.state != ServiceState.PREPARING) {
            i18n.error("agent.local-runtime.factory.boot.error", service.name(), service.state)
            return
        }

        i18n.info("agent.local-runtime.factory.boot.up", service.name())

        val platform = service.group.platform()

        service.state = ServiceState.STARTING
        service.path.createDirectories()

        val environment = HashMap<String, String>()
        environment["hostname"] = service.hostname
        environment["port"] = service.port.toString()
        environment["service-name"] = service.name()
        environment["velocityProxyToken"] = Agent.instance.securityProvider.proxySecureToken

        // find a better way here
        environment["velocity_use"] = Agent.instance.runtime.groupStorage().items().stream().anyMatch { it -> it.platform().name.equals("velocity") }.toString()
        environment["version"] = Agent.instance.version()

        // copy all templates to the service path
        Agent.instance.runtime.templates().bindTemplate(service)

        // download and copy the platform files to the service path
        platform.prepare(service.path, service.group.data.platform.version, environment)


        val serverIconPath = service.path.resolve("server-icon.png")
        // copy server-icon if not exists
        if (Files.notExists(serverIconPath)) {
            Files.copy(this.javaClass.classLoader.getResourceAsStream("server-icon.png")!!, serverIconPath)
        }

        // basically current only the java command is supported yet
        val commands = getLanguageSpecificCommands(platform, service)

        val processBuilder = ProcessBuilder(commands).directory(service.path.toFile())
        processBuilder.environment().putAll(environment)

        service.process = processBuilder.start()
        service.startTracking()
    }

    @OptIn(ExperimentalPathApi::class)
    override fun shutdownApplication(service: LocalService, shutdownCleanUp: Boolean) {
        if (service.state == ServiceState.STOPPING || service.state == ServiceState.STOPPED) {
            return
        }
        service.state = ServiceState.STOPPING

        i18n.info("agent.local-runtime.factory.shutdown", service.name())

        val eventService = Agent.instance.eventService

        eventService.dropServiceSubscriptions(service)
        eventService.call(ServiceShutdownEvent(service))

        if (service.process != null) {
            try {
                if (shutdownCleanUp && service.executeCommand(service.group.platform().shutdownCommand)) {
                    if (service.process!!.waitFor(5, TimeUnit.SECONDS)) {
                        service.process!!.exitValue()
                        service.state == ServiceState.STOPPED
                    }
                }
            } catch (_: Exception) {
                // ignore exceptions, we just want to stop the process
            }

            if (service.state != ServiceState.STOPPED) {
                service.process!!.toHandle().destroyForcibly()
                service.process = null
                service.state == ServiceState.STOPPED
            }
        }

        localRuntime.terminal.screenService.stopCurrentRecording()
        service.stopTracking()

        // windows need some time to destroy the process
        if (!Thread.currentThread().isVirtual && shutdownCleanUp) {
            Thread.sleep(200) // wait for a process to be destroyed
        }

        service.path.deleteRecursively()

        service.state = ServiceState.STOPPED
        Agent.instance.runtime.serviceStorage().dropService(service)
        i18n.info("agent.local-runtime.factory.shutdown.successful", service.name())
    }

    private fun getLanguageSpecificCommands(platform: Platform, service: Service): ArrayList<String> {
        val commands = ArrayList<String>()

        when (platform.language) {
            PlatformLanguage.JAVA -> {
                val javaPath = System.getProperty("java.home")

                commands.add("${javaPath}/bin/java")
                commands.addAll(
                    listOf(
                        "-Dterminal.jline=false",
                        "-Dfile.encoding=UTF-8",
                        "-Xms" + service.group.data.minMemory + "M",
                        "-Xmx" + service.group.data.maxMemory + "M",
                        "-jar",
                        service.group.applicationPlatformFile().name
                    )
                )
                commands.addAll(platform.arguments)
            }

            PlatformLanguage.GO, PlatformLanguage.RUST -> {
                commands.add("./${service.group.applicationPlatformFile().name}")
            }
        }
        return commands
    }
}