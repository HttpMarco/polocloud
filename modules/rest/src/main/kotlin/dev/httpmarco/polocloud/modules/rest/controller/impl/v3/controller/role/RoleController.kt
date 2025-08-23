package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.role

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.RestModule
import dev.httpmarco.polocloud.modules.rest.auth.user.User
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.role.RoleCreateModel
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.role.RoleEditModel
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import io.javalin.http.Context

class RoleController : Controller("/role") {

    @Request(requestType = RequestType.POST, path = "/", permission = "polocloud.role.create")
    fun createRole(context: Context, user: User) {
        val roleCreateModel = try {
            context.bodyAsClass(RoleCreateModel::class.java)
        } catch (e: Exception) {
            context.status(400).json(message("Invalid body"))
            return
        }

        if (roleCreateModel.label.isBlank() || roleCreateModel.hexColor.isBlank()) {
            context.status(400).json(message("Invalid body: missing fields"))
            return
        }

        if ("*" in roleCreateModel.permissions && user.role?.permissions?.contains("*") == false) {
            context.status(403).json(message("You cannot assign * permission"))
            return
        }

        val role = RestModule.instance.roleProvider.createRole(
            roleCreateModel.label,
            roleCreateModel.hexColor,
            roleCreateModel.permissions.toMutableList()
        )

        if (role == null) {
            context.status(400).json(message("Role already exists"))
            return
        }

        context.status(201).json(
            JsonObject().apply {
                addProperty("id", role.id)
                addProperty("label", role.label)
                addProperty("hexColor", role.hexColor)
                addProperty("default", role.default)
            }.toString()
        )
    }

    @Request(requestType = RequestType.DELETE, path = "/{id}", permission = "polocloud.role.delete")
    fun deleteRole(context: Context) {
        val id = context.pathParam("id").toIntOrNull()
        if (id == null) {
            context.status(400).json(message("Invalid role ID"))
            return
        }

        val role = RestModule.instance.roleProvider.roleById(id)
        if (role == null || role.default) {
            context.status(404).json(message("Role not found or cannot be deleted"))
            return
        }

        if (RestModule.instance.roleProvider.deleteRole(role)) {
            context.status(204).json(message("Role deleted successfully"))
        } else {
            context.status(500).json(message("Failed to delete role"))
        }
    }

    @Request(requestType = RequestType.PATCH, path = "/{id}", permission = "polocloud.role.edit")
    fun editRole(context: Context, user: User) {
        val id = context.pathParam("id").toIntOrNull()
        if (id == null) {
            context.status(400).json(message("Invalid role ID"))
            return
        }

        val roleEditModel = try {
            context.bodyAsClass(RoleEditModel::class.java)
        } catch (e: Exception) {
            context.status(400).json(message("Invalid body"))
            return
        }

        if (roleEditModel.label.isBlank() || roleEditModel.hexColor.isBlank()) {
            context.status(400).json(message("Invalid body: missing fields"))
            return
        }

        val role = RestModule.instance.roleProvider.roleById(id)
        if (role == null) {
            context.status(404).json(message("Role not found"))
            return
        }

        if ("*" in roleEditModel.permissions && user.role?.permissions?.contains("*") == false) {
            context.status(403).json(message("You cannot assign * permission"))
            return
        }

        role.label = roleEditModel.label
        role.hexColor = roleEditModel.hexColor

        val currentPermissions = role.permissions.toMutableSet()
        val newPermissions = roleEditModel.permissions.toSet()

        val toRemove = currentPermissions - newPermissions
        role.permissions.removeAll(toRemove)

        val toAdd = newPermissions - currentPermissions
        role.permissions.addAll(toAdd)

        RestModule.instance.roleProvider.editRole(role)

        context.status(200).json(
            JsonObject().apply {
                addProperty("id", role.id)
                addProperty("label", role.label)
                addProperty("hexColor", role.hexColor)
                addProperty("default", role.default)
                add("permissions", JsonArray().apply {
                    role.permissions.forEach { permission ->
                        add(permission)
                    }
                })
            }.toString()
        )
    }

    @Request(requestType = RequestType.GET, path = "s/", permission = "polocloud.role.list")
    fun listRoles(context: Context) {
        context.status(200).json(
            JsonArray().apply {
                RestModule.instance.roleProvider.roles().forEach { role ->
                    add(JsonObject().apply {
                        addProperty("id", role.id.toString())
                        addProperty("label", role.label)
                        addProperty("hexColor", role.hexColor)
                        addProperty("default", role.default)
                        addProperty("userCount", RestModule.instance.userProvider.roleCount(role.id))
                    })
                }
            }.toString()
        )
    }

    @Request(requestType = RequestType.GET, path = "/{id}", permission = "polocloud.role.get")
    fun getRole(context: Context) {
        val id = context.pathParam("id").toIntOrNull()
        if (id == null) {
            context.status(400).json(message("Invalid role ID"))
            return
        }

        val role = RestModule.instance.roleProvider.roleById(id)
        if (role == null) {
            context.status(404).json(message("Role not found"))
            return
        }

        context.status(200).json(
            JsonObject().apply {
                addProperty("id", role.id)
                addProperty("label", role.label)
                addProperty("hexColor", role.hexColor)
                addProperty("default", role.default)
                add("permissions", JsonArray().apply {
                    role.permissions.forEach { permission ->
                        add(permission)
                    }
                })
            }.toString()
        )
    }
}