package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.role

import com.google.gson.JsonArray
import dev.httpmarco.polocloud.modules.rest.RestModule
import dev.httpmarco.polocloud.modules.rest.auth.role.toJson
import dev.httpmarco.polocloud.modules.rest.auth.user.User
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.defaultResponse
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.role.RoleCreateModel
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.role.RoleEditModel
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import io.javalin.http.Context

class RoleController : Controller("/role") {

    @Request(requestType = RequestType.POST, path = "/", permission = "polocloud.role.create")
    fun createRole(context: Context, user: User) {
        val model = context.parseBodyOrBadRequest<RoleCreateModel>() ?: return
        if (!context.validate(model.label.isNotBlank(), "Role label is required")) return
        if (!context.validate(model.hexColor.isNotBlank(), "Role hexColor is required")) return

        if ("*" in model.permissions && user.role?.permissions?.contains("*") == true) {
            context.defaultResponse(403, "You cannot assign * permission")
            return
        }

        val role = RestModule.instance.roleProvider.createRole(model.label, model.hexColor, model.permissions.toMutableList())
        if (role == null) {
            context.defaultResponse(400, "Role already exists")
            return
        }

        context.defaultResponse(201, data = role.toJson())
    }

    @Request(requestType = RequestType.DELETE, path = "/{id}", permission = "polocloud.role.delete")
    fun deleteRole(context: Context) {
        val id = context.pathParam("id").toIntOrNull()
        if (id == null) {
            context.defaultResponse(400, "Invalid role ID")
            return
        }

        val role = RestModule.instance.roleProvider.roleById(id)
        if (role == null || role.default) {
            context.defaultResponse(404, "Role not found or cannot be deleted")
            return
        }

        if (RestModule.instance.roleProvider.deleteRole(role)) {
            context.defaultResponse(204)
            return
        }

        context.defaultResponse(500, "Failed to delete role")
    }

    @Request(requestType = RequestType.PATCH, path = "/{id}", permission = "polocloud.role.edit")
    fun editRole(context: Context, user: User) {
        val id = context.pathParam("id").toIntOrNull()
        if (id == null) {
            context.defaultResponse(400,"Invalid role ID")
            return
        }

        val model = context.parseBodyOrBadRequest<RoleEditModel>() ?: return
        if (!context.validate(model.label.isNotBlank(), "Role label is required")) return
        if (!context.validate(model.hexColor.isNotBlank(), "Role hexColor is required")) return

        val role = context.validateRole(id) ?: return

        if ("*" in model.permissions && user.role?.permissions?.contains("*") == true) {
            context.defaultResponse(403,"You cannot assign * permission")
            return
        }

        role.label = model.label
        role.hexColor = model.hexColor

        val currentPermissions = role.permissions.toMutableSet()
        val newPermissions = model.permissions.toSet()

        val toRemove = currentPermissions - newPermissions
        role.permissions.removeAll(toRemove)

        val toAdd = newPermissions - currentPermissions
        role.permissions.addAll(toAdd)

        RestModule.instance.roleProvider.editRole(role)

        context.defaultResponse(200, data = role.toJson())
    }

    @Request(requestType = RequestType.GET, path = "s/", permission = "polocloud.role.list")
    fun listRoles(context: Context) {
        val data = JsonArray().apply {
            RestModule.instance.roleProvider.roles().forEach { role ->
                add(role.toJson().apply {
                    addProperty("userCount", RestModule.instance.userProvider.roleCount(role.id))
                })
            }
        }

        context.defaultResponse(200, data = data)
    }

    @Request(requestType = RequestType.GET, path = "/{id}", permission = "polocloud.role.get")
    fun getRole(context: Context) {
        val id = context.pathParam("id").toIntOrNull()
        if (id == null) {
            context.defaultResponse(400, "Invalid role ID")
            return
        }

        val role = context.validateRole(id) ?: return

        context.defaultResponse(200, data = role.toJson())
    }
}