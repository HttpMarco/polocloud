package dev.httpmarco.polocloud.modules.rest.controller

import io.javalin.http.HandlerType

data class OpenEndpoint(
    val path: String,
    val method: HandlerType,
    val condition: () -> Boolean = { true }
)