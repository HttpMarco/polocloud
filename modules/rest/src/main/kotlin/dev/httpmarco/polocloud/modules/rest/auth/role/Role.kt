package dev.httpmarco.polocloud.modules.rest.auth.role

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.auth.user.permission.Permission

data class Role(
    val id: Int,
    var label: String,
    var hexColor: String,
    val default: Boolean = false,
) : Permission()


fun Role.toJson(): JsonObject {
    val rolePermissions = JsonArray().apply {
        permissions.forEach { permission ->
            add(permission)
        }
    }

    return JsonObject().apply {
        addProperty("id", id)
        addProperty("label", label)
        addProperty("hexColor", hexColor)
        addProperty("default", default)
        add("permissions", rolePermissions)
    }
}