package dev.httpmarco.polocloud.modules.rest

import io.javalin.Javalin

class HttpServer {

    private var app: Javalin? = null

    fun start() {
        this.app = Javalin.create { cfg ->
            cfg.jetty.defaultHost = config.javalinConfiguration.hostname
            cfg.jetty.defaultPort = config.javalinConfiguration.port
            cfg.showJavalinBanner = false

            cfg.jetty.modifyServer { server ->
                server.stopTimeout = 5000
                server.stopAtShutdown = true
            }
        }.apply {
            ExceptionHandler.register(this)
        }.start()

        logger.info("HTTP server started on port ${app?.port()}")
    }

    fun stop() {
        this.app?.stop()
        logger.info("HTTP server stopped.")
    }
}