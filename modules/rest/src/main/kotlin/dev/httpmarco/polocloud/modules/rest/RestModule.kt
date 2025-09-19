package dev.httpmarco.polocloud.modules.rest

import dev.httpmarco.polocloud.modules.rest.auth.JwtProvider
import dev.httpmarco.polocloud.modules.rest.auth.role.RoleProvider
import dev.httpmarco.polocloud.modules.rest.auth.user.UserProvider
import dev.httpmarco.polocloud.modules.rest.config.RestConfiguration
import dev.httpmarco.polocloud.modules.rest.config.ConfigProvider
import dev.httpmarco.polocloud.modules.rest.config.Users
import dev.httpmarco.polocloud.modules.rest.controller.ControllerProvider
import dev.httpmarco.polocloud.modules.rest.socket.web.WebSocketService
import dev.httpmarco.polocloud.shared.module.PolocloudModule
import dev.httpmarco.polocloud.shared.logging.Logger
import dev.httpmarco.polocloud.shared.polocloudShared
import java.nio.file.Files
import java.nio.file.Path

val logger: Logger = polocloudShared.logger()
val config: RestConfiguration = ConfigProvider().read("local/modules/rest/config", RestConfiguration())
val usersConfiguration: Users = ConfigProvider().read("local/modules/rest/users", Users())

class RestModule : PolocloudModule {

    private val configPath = Path.of("local/modules/rest")

    companion object {
        lateinit var instance: RestModule
            private set
    }

    lateinit var httpServer: HttpServer
    lateinit var jwtProvider: JwtProvider
    lateinit var controllerProvider: ControllerProvider
    lateinit var webSocketService: WebSocketService
    lateinit var userProvider: UserProvider
    lateinit var roleProvider: RoleProvider

    init {
        if (Files.notExists(this.configPath)) {
            Files.createDirectories(this.configPath)
        }
    }

    override fun onEnable() {
        instance = this

        this.httpServer = HttpServer()
        this.httpServer.start()

        this.jwtProvider = JwtProvider(usersConfiguration.secret)
        this.controllerProvider = ControllerProvider()
        this.webSocketService = WebSocketService()

        this.userProvider = UserProvider()
        this.roleProvider = RoleProvider()

        logger.info("Rest module started.")
    }

    override fun onDisable() {
        this.webSocketService.shutdown()
        this.httpServer.stop()
        logger.info("Rest module stopped.")
    }
}