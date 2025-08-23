package dev.httpmarco.polocloud.modules.rest

import io.javalin.Javalin

class HttpServer {

    lateinit var app: Javalin
        private set

    fun start() {
        this.app = Javalin.create { cfg ->
            cfg.jetty.defaultHost = config.hostname
            cfg.jetty.defaultPort = config.port
            cfg.showJavalinBanner = false

            cfg.jetty.modifyServer { server ->
                server.stopTimeout = 5000
                server.stopAtShutdown = true
            }

            cfg.requestLogger.http { ctx, executionTimeMs ->
                logger.debug("[HTTP] ${ctx.method()} ${ctx.path()} -> ${ctx.status()} (${executionTimeMs} ms)")
            }

            cfg.requestLogger.ws { wsConfig ->
                wsConfig.onConnect { ws ->
                    logger.debug("[WS] Connected: ${ws.session.remoteAddress}")
                }

                wsConfig.onClose { ws ->
                    logger.debug("[WS] Closed: ${ws.session.remoteAddress} (status=${ws.status()}, reason=${ws.reason()})")
                }

                wsConfig.onError { ws ->
                    logger.debug("[WS] Error: ${ws.session.remoteAddress} ${ws.error()}")
                }
            }
        }.apply {
            ExceptionHandler.register(this)
        }.start()

        logger.info("HTTP server started on port ${app.port()}")
    }

    fun stop() {
        this.app.stop()
        logger.info("HTTP server stopped.")
    }
}