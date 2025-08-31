package dev.httpmarco.polocloud.modules.rest

import io.javalin.Javalin
import io.javalin.community.ssl.SslPlugin
import java.nio.file.Files
import java.nio.file.Paths

class HttpServer {

    lateinit var app: Javalin
        private set

    fun start() {
        this.app = Javalin.create { cfg ->
            cfg.jetty.defaultHost = config.hostname
            cfg.jetty.defaultPort = config.port
            cfg.showJavalinBanner = false

            if (config.sslEnabled) {
                require(config.certPath.isNotBlank() && config.keyPath.isNotBlank()) {
                    logger.info("SSL is enabled, but Cert or Key Path is empty")
                }

                val certFile = Paths.get(config.certPath)
                val keyFile = Paths.get(config.keyPath)

                require(Files.exists(certFile) && Files.exists(keyFile)) {
                    logger.info("Missing Cert or Key file for ssl")
                }

                val sslPlugin = SslPlugin { sslCfg ->
                    sslCfg.pemFromPath(certFile.toString(), keyFile.toString())

                    sslCfg.insecurePort = config.port
                    sslCfg.securePort = config.securePort

                    sslCfg.redirect = true

                    sslCfg.http2 = false
                    sslCfg.sniHostCheck = false
                }

                cfg.registerPlugin(sslPlugin)
            }

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

        logger.info("Server started on ${if (config.sslEnabled) "HTTPS port ${config.securePort} (redirect from HTTP ${config.port})" else "HTTP port ${config.port}"}")
    }

    fun stop() {
        this.app.stop()
        logger.info("HTTP server stopped.")
    }
}