package dev.httpmarco.polocloud.modules.rest

import com.google.gson.JsonObject
import io.javalin.Javalin
import org.slf4j.LoggerFactory

object ExceptionHandler {

    private val logger = LoggerFactory.getLogger(ExceptionHandler::class.java)

    fun register(app: Javalin) {
        app.exception(Exception::class.java) { e, ctx ->
            ctx.status(500).json(JsonObject().apply { addProperty("message", "Internal Server Error") }.toString())
            logger.error("An error occurred while processing the request", e)
        }
    }
}