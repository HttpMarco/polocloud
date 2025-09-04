package dev.httpmarco.polocloud.modules.rest.controller

import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.RestModule
import dev.httpmarco.polocloud.modules.rest.auth.role.Role
import io.javalin.http.Context
import java.util.UUID

abstract class Controller(
    val path: String
) {

    fun message(message: String): String = JsonObject().apply { addProperty("message", message) }.toString()

    fun isNumber(number: String): Boolean = number.toIntOrNull() != null

    fun Context.validate(condition: Boolean, message: String): Boolean {
        if (condition) return true
        this.defaultResponse(400, message)
        return false
    }

    fun Context.validateRole(roleId: Int): Role? {
        val role = RestModule.instance.roleProvider.roleById(roleId)
        if (role == null) this.defaultResponse(400, "Invalid role ID")
        return role
    }

    fun Context.parseUUID(param: String): UUID? {
        return try {
            UUID.fromString(this.pathParam(param))
        } catch (e: IllegalArgumentException) {
            this.defaultResponse(400, "Invalid UUID format")
            null
        }
    }

    inline fun <reified T> Context.parseBodyOrBadRequest(): T? {
        return try {
            this.bodyAsClass(T::class.java)
        } catch (e: Exception) {
            this.status(400).json(message("Invalid body"))
            null
        }
    }
}
