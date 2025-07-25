package dev.httpmarco.polocloud.agent

import dev.httpmarco.polocloud.agent.configuration.AgentConfig
import dev.httpmarco.polocloud.agent.detector.DetectorFactoryThread
import dev.httpmarco.polocloud.agent.detector.OnlineStateDetector
import dev.httpmarco.polocloud.agent.events.EventService
import dev.httpmarco.polocloud.agent.grpc.GrpcServerEndpoint
import dev.httpmarco.polocloud.agent.i18n.I18nPolocloudAgent
import dev.httpmarco.polocloud.agent.logging.Logger
import dev.httpmarco.polocloud.agent.runtime.Runtime
import dev.httpmarco.polocloud.agent.security.SecurityProvider
import dev.httpmarco.polocloud.platforms.PlatformPool

// global terminal instance for the agent
// this is used to print messages to the console
val logger = Logger()
val developmentMode = System.getProperty("polocloud.version", "false").toBoolean()
val i18n = I18nPolocloudAgent()

class Agent {

    val runtime: Runtime
    val config: AgentConfig
    val eventService = EventService()
    val securityProvider = SecurityProvider()

    private val grpcServerEndpoint = GrpcServerEndpoint()
    private val onlineStateDetector = DetectorFactoryThread.bindDetector(OnlineStateDetector())

    companion object {
        val instance = Agent()
    }

    init {
        // display the default log information
        i18n.info("agent.starting", version())

        if (version().endsWith("-SNAPSHOT")) {
            i18n.warn("agent.version.warn")
        }

        this.runtime = Runtime.create()
        this.grpcServerEndpoint.connect()

        // read all information about the runtime config
        this.config = this.runtime.configHolder().read("config", AgentConfig())

        i18n.info("agent.starting.runtime", runtime::class.simpleName)

        val groups = runtime.groupStorage().items()
        i18n.info(
            "agent.starting.groups.count",
            groups.size,
            groups.joinToString(separator = "&8, &7") { it.data.name })
        i18n.info("agent.starting.platforms.count", PlatformPool.size(), PlatformPool.versionSize())

        i18n.info("agent.starting.successful")

        this.onlineStateDetector.detect()
    }

    fun close() {
        this.runtime.shutdown()
        this.grpcServerEndpoint.close()
        this.onlineStateDetector.close()
    }

    fun version(): String {
        return System.getenv("polocloud-version")
    }
}