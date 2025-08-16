package dev.httpmarco.polocloud.modules.rest.controller

import com.google.gson.JsonObject

abstract class Controller(
    val path: String
) {

    fun message(message: String): String = JsonObject().apply { addProperty("message", message) }.toString()

    fun isNumber(number: String): Boolean = number.toIntOrNull() != null

}