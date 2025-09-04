package dev.httpmarco.polocloud.modules.rest.controller

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.javalin.http.Context

fun Context.defaultResponse(statusCode: Int, message: String? = null, data: JsonElement? = null) {
    val response = JsonObject()
    message?.let { response.addProperty("message", it) }
    response.addProperty("status", statusCode)
    data?.let { response.add("data", it) }

    this.status(statusCode).json(response.toString())
}