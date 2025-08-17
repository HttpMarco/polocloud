package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.user

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.RestModule
import dev.httpmarco.polocloud.modules.rest.auth.EncryptionUtil
import dev.httpmarco.polocloud.modules.rest.auth.user.User
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.user.UserCreateModel
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.user.UserEditModel
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.user.UserSelfEditModel
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import io.javalin.http.Context
import io.javalin.http.Cookie
import java.util.UUID
import java.util.concurrent.TimeUnit

class UserController : Controller("/user") {

    @Request(requestType = RequestType.POST, path = "/", permission = "polocloud.user.create")
    fun createUser(context: Context) {
        val userCreateModel = try {
            context.bodyAsClass(UserCreateModel::class.java)
        } catch (e: Exception) {
            context.status(400).json(message("Invalid body"))
            return
        }

        if (userCreateModel.username.isBlank() || userCreateModel.password.isBlank()) {
            context.status(400).json(message("Invalid body: missing fields"))
            return
        }

        val hashedPassword = EncryptionUtil.encrypt(userCreateModel.password)
        val role = RestModule.instance.roleProvider.roleById(userCreateModel.roleId)
        val user = User(UUID.randomUUID(), userCreateModel.username, role, hashedPassword, System.currentTimeMillis())

        val token = RestModule.Companion.instance.userProvider.create(user, context.ip(), context.userAgent())
        if (token == null) {
            context.status(400).json(message("User already exists"))
            return
        }

        val cookie = Cookie(
            "token",
            token.value,
            maxAge = TimeUnit.DAYS.toSeconds(7).toInt(),
            isHttpOnly = true,
            secure = true,
        )

        context.status(201).cookie(cookie).json(message("User created"))
    }

    @Request(requestType = RequestType.PATCH, path = "/self/edit")
    fun selfEdit(context: Context, user: User) {
        val userSelfEditModel = try {
            context.bodyAsClass(UserSelfEditModel::class.java)
        } catch (e: Exception) {
            context.status(400).json(message("Invalid body"))
            return
        }

        if (userSelfEditModel.username.isBlank() || userSelfEditModel.password.isBlank()) {
            context.status(400).json(message("Invalid body: missing fields"))
            return
        }

        val hashedPassword = EncryptionUtil.encrypt(userSelfEditModel.password)

        user.username = userSelfEditModel.username
        user.passwordHash = hashedPassword

        RestModule.instance.userProvider.edit(user)
        context.status(201).json(message("User updated"))
    }

    @Request(requestType = RequestType.PATCH, path = "/edit", permission = "polocloud.user.edit")
    fun edit(context: Context) {
        val userEditModel = try {
            context.bodyAsClass(UserEditModel::class.java)
        } catch (e: Exception) {
            context.status(400).json(message("Invalid body"))
            return
        }

        if (userEditModel.uuid.isEmpty()) {
            context.status(400).json(message("Invalid body: missing fields"))
            return
        }

        val userUUID = UUID.fromString(userEditModel.uuid)
        val user = RestModule.instance.userProvider.userByUUID(userUUID)
        if (user == null) {
            context.status(404).json(message("User not found"))
            return
        }

        val role = RestModule.instance.roleProvider.roleById(userEditModel.roleId)
        if (role == null) {
            context.status(400).json(message("Invalid role ID"))
            return
        }

        user.role = role

        RestModule.instance.userProvider.edit(user)
        context.status(201).json(message("User updated"))
    }

    @Request(requestType = RequestType.DELETE, path = "/{uuid}", permission = "polocloud.user.delete")
    fun delete(context: Context) {
        val uuidString = context.pathParam("uuid")
        val userUUID = try {
            UUID.fromString(uuidString)
        } catch (e: IllegalArgumentException) {
            context.status(400).json(message("Invalid UUID format"))
            return
        }

        RestModule.instance.userProvider.delete(userUUID)
        context.status(204).json(message("User deleted"))
    }

    @Request(requestType = RequestType.DELETE, path = "/self")
    fun deleteSelf(context: Context, user: User) {
        RestModule.instance.userProvider.delete(user.uuid)
        context.removeCookie("token")
        context.status(204).json(message("User deleted"))
    }

    @Request(requestType = RequestType.GET, path = "s/", permission = "polocloud.user.list")
    fun list(context: Context) {
        context.status(200).json(
            JsonArray().apply {
                RestModule.instance.userProvider.users().forEach { user ->
                    add(JsonObject().apply {
                        addProperty("uuid", user.uuid.toString())
                        addProperty("username", user.username)
                        addProperty("createdAt", user.createdAt)
                        addProperty("role", user.role?.id ?: 0)
                    })
                }
            }.toString()
        )
    }

    @Request(requestType = RequestType.GET, path = "/{uuid}", permission = "polocloud.user.get")
    fun getUser(context: Context) {
        val uuidString = context.pathParam("uuid")
        val userUUID = try {
            UUID.fromString(uuidString)
        } catch (e: IllegalArgumentException) {
            context.status(400).json(message("Invalid UUID format"))
            return
        }

        val user = RestModule.instance.userProvider.userByUUID(userUUID)
        if (user == null) {
            context.status(404).json(message("User not found"))
            return
        }

        context.status(200).json(
            JsonObject().apply {
                addProperty("uuid", user.uuid.toString())
                addProperty("username", user.username)
                addProperty("createdAt", user.createdAt)
                addProperty("role", user.role?.id ?: 0)
            }
        )
    }

    @Request(requestType = RequestType.GET, path = "/self")
    fun self(context: Context, user: User) {
        context.status(200).json(
            JsonObject().apply {
                addProperty("uuid", user.uuid.toString())
                addProperty("username", user.username)
                addProperty("createdAt", user.createdAt)
                addProperty("role", user.role?.id ?: 0)
            }.toString()
        )
    }

    @Request(requestType = RequestType.GET, path = "/tokens")
    fun tokens(context: Context, user: User) {
        context.status(200).json(
            JsonArray().apply {
                user.tokens.forEach { token ->
                    add(JsonObject().apply {
                        addProperty("ip", token.data.ip)
                        addProperty("userUUID", token.data.userUUID.toString())
                        addProperty("userAgent", token.data.userAgent)
                        addProperty("lastActivity", token.data.lastActivity)
                    })
                }
            }.toString()
        )
    }
}