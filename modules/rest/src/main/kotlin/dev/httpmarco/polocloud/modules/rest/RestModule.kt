package dev.httpmarco.polocloud.modules.rest

import dev.httpmarco.polocloud.modules.rest.config.Config
import dev.httpmarco.polocloud.modules.rest.config.ConfigProvider
import dev.httpmarco.polocloud.shared.module.PolocloudModule
import dev.httpmarco.polocloud.shared.logging.Logger
import dev.httpmarco.polocloud.shared.polocloudShared
import java.nio.file.Files
import java.nio.file.Path

val logger: Logger = polocloudShared.logger()
val config: Config = ConfigProvider().read("local/modules/rest/config", Config())

class RestModule : PolocloudModule {

    private val configPath = Path.of("local/modules/rest")
    lateinit var httpServer: HttpServer
        private set

    init {
        if (Files.notExists(this.configPath)) {
            Files.createDirectories(this.configPath)
        }
    }

    override fun onEnable() {
        this.httpServer = HttpServer()

        this.httpServer.start()
        logger.info("Rest module started.")
    }

    override fun onDisable() {
        this.httpServer.stop()
        logger.info("Rest module stopped.")
    }
}