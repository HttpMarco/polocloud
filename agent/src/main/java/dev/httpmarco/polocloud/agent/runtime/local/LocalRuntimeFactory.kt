package dev.httpmarco.polocloud.agent.runtime.local

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.events.definitions.ServiceShutdownEvent
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.RuntimeFactory
import dev.httpmarco.polocloud.agent.services.Service
import dev.httpmarco.polocloud.platforms.PlatformType
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

        if (service.state != Service.State.PREPARING) {
            logger.error("Cannot boot application for service ${service.name()} because it is not in PREPARING state, but in ${service.state} state&8. &7Wait for action&8...")
            return
        }

        logger.info("The service &3${service.name()}&7 is now starting&8...")

        val platform = service.group.platform()

        service.state = Service.State.STARTING
        service.path.createDirectories()

        val environment = HashMap<String, String>()
        environment.put("hostname", service.hostname)
        environment.put("port", service.port.toString())
        environment.put("service-name", service.name())
        environment.put("need-bridge", (service.group.platform().type == PlatformType.PROXY).toString())
        environment.put("velocityProxyToken", Agent.instance.securityProvider.proxySecureToken)

        // copy all templates to the service path
        Agent.instance.runtime.templates().bindTemplate(service)

        // download and copy the platform files to the service path
        platform.prepare(service.path, service.group.data.platform.version, environment)


        val serverIconPath = service.path.resolve("server-icon.png")
        // copy server-icon if not exists
        if(Files.notExists(serverIconPath)) {
            Files.copy(this.javaClass.classLoader.getResourceAsStream("server-icon.png")!!, serverIconPath)
        }

        // basically current only the java command is supported yet
        val commands = ArrayList<String>()
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

        val processBuilder = ProcessBuilder(commands).directory(service.path.toFile())
        processBuilder.environment().putAll(environment)

        service.process = processBuilder.start()
        service.startTracking()
    }

    @OptIn(ExperimentalPathApi::class)
    override fun shutdownApplication(service: LocalService, shutdownCleanUp : Boolean) {
        if (service.state == Service.State.STOPPING || service.state == Service.State.STOPPING) {
            return
        }
        service.state = Service.State.STOPPING

        logger.info("The service &3${service.name()}&7 is now stopping&8...")

        val eventService = Agent.instance.eventService

        eventService.dropServiceSubscriptions(service)
        eventService.call(ServiceShutdownEvent(service))

        if (service.process != null) {
            try {
                if (shutdownCleanUp && service.executeCommand(service.group.platform().shutdownCommand)) {
                    if (service.process!!.waitFor(5, TimeUnit.SECONDS)) {
                        service.process!!.exitValue()
                        service.state == Service.State.STOPPED
                    }
                }
            } catch (_: Exception) {
                // ignore exceptions, we just want to stop the process
            }

            if (service.state != Service.State.STOPPED) {
                service.process!!.toHandle().destroyForcibly()
                service.process = null
                service.state == Service.State.STOPPED
            }
        }

        localRuntime.terminal.screenService.stopCurrentRecording()
        service.stopTracking()

        // windows need some time to destroy the process
        if (!Thread.currentThread().isVirtual && shutdownCleanUp) {
            Thread.sleep(200) // wait for a process to be destroyed
        }

        service.path.deleteRecursively()

        service.state = Service.State.STOPPED
        Agent.instance.runtime.serviceStorage().dropService(service)
        logger.info("The service &3${service.name()}&7 has been stopped and deleted&8.")
    }
}