package dev.httpmarco.polocloud.modules.rest

import dev.httpmarco.polocloud.modules.rest.controller.defaultResponse
import io.javalin.Javalin
import org.slf4j.LoggerFactory

object ExceptionHandler {

    private val logger = LoggerFactory.getLogger(ExceptionHandler::class.java)

    fun register(app: Javalin) {
        app.exception(Exception::class.java) { e, context ->
            context.defaultResponse(500, "Internal Server Error")
            logger.error("An error occurred while processing the request", e)
        }
    }
}