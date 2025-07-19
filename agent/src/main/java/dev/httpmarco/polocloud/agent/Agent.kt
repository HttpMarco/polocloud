package dev.httpmarco.polocloud.agent

import dev.httpmarco.polocloud.agent.detector.DetectorFactoryThread
import dev.httpmarco.polocloud.agent.detector.OnlineStateDetector
import dev.httpmarco.polocloud.agent.events.EventService
import dev.httpmarco.polocloud.agent.grpc.GrpcServerEndpoint
import dev.httpmarco.polocloud.agent.i18n.I18nPolocloudAgent
import dev.httpmarco.polocloud.agent.logging.Logger
import dev.httpmarco.polocloud.agent.runtime.Runtime
import dev.httpmarco.polocloud.agent.security.SecurityProvider
import dev.httpmarco.polocloud.platforms.PlatformPool
import dev.httpmarco.polocloud.platforms.tasks.PlatformTaskPool

// global terminal instance for the agent
// this is used to print messages to the console
val logger = Logger()
val developmentMode = System.getProperty("polocloud.version", "false").toBoolean()
val i18n = I18nPolocloudAgent();

class Agent {

    val runtime: Runtime
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

        if(version().endsWith("-SNAPSHOT")) {
            logger.warn("You are using a snapshot version of polocloud. This version is not recommended for production use!")
        }

        this.runtime = Runtime.create()
        this.grpcServerEndpoint.connect()

        logger.info("Using runtime: ${runtime::class.simpleName}")

        val groups = runtime.groupStorage().items()
        logger.info("Load groups&8 (&7${groups.size}&8): &7" + groups.joinToString(separator = "&8, &7") { it.data.name })
        logger.info("Load ${PlatformPool.size()} platforms with ${PlatformTaskPool.size()} tasks&8.")

        logger.info("The agent is now &3successfully &7started and ready to use&8!")

        this.onlineStateDetector.detect()
        this.runtime.postInitialize()
    }

    fun close() {
        this.runtime.shutdown()
        this.grpcServerEndpoint.close()
        this.onlineStateDetector.close()
    }

    fun version(): String {
        return System.getenv("polocloud-version");
    }
}