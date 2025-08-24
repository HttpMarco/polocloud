package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.user

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.RestModule
import dev.httpmarco.polocloud.modules.rest.auth.EncryptionUtil
import dev.httpmarco.polocloud.modules.rest.auth.user.User
import dev.httpmarco.polocloud.modules.rest.auth.user.token.Token
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.user.UserCreateModel
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.user.UserSelfCreateModel
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.user.UserEditModel
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.user.UserPasswordChangeModel
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

        if (userCreateModel.username.isBlank()) {
            context.status(400).json(message("Invalid body: missing fields"))
            return
        }

        val role = RestModule.instance.roleProvider.roleById(userCreateModel.roleId)
        if (role == null) {
            context.status(400).json(message("Invalid role ID"))
            return
        }

        val password = RestModule.instance.userProvider.create(userCreateModel.username, role)
        context.status(201).json(JsonObject().apply { addProperty("password", password) }.toString())
    }

    @Request(requestType = RequestType.POST, path = "/self")
    fun createSelfUser(context: Context) {
        val userSelfCreateModel = try {
            context.bodyAsClass(UserSelfCreateModel::class.java)
        } catch (e: Exception) {
            context.status(400).json(message("Invalid body"))
            return
        }

        if (userSelfCreateModel.username.isBlank() || userSelfCreateModel.password.isBlank()) {
            context.status(400).json(message("Invalid body: missing fields"))
            return
        }

        if (RestModule.instance.userProvider.users().isNotEmpty()) {
            context.status(400).json(message("A user already exists"))
            return
        }

        val hashedPassword = EncryptionUtil.encrypt(userSelfCreateModel.password)
        val role = RestModule.instance.roleProvider.roleById(userSelfCreateModel.roleId)
        val user = User(UUID.randomUUID(), userSelfCreateModel.username, role, hashedPassword, true, System.currentTimeMillis())

        val token = RestModule.instance.userProvider.createSelf(user, context.ip(), context.userAgent())
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

    @Request(requestType = RequestType.PATCH, path = "/self/edit", permission = "polocloud.user.self.edit")
    fun selfEdit(context: Context, user: User) {
        val userSelfEditModel = try {
            context.bodyAsClass(UserSelfEditModel::class.java)
        } catch (e: Exception) {
            context.status(400).json(message("Invalid body"))
            return
        }

        if (userSelfEditModel.username.isBlank()) {
            context.status(400).json(message("Invalid body: missing fields"))
            return
        }

        user.username = userSelfEditModel.username

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

        if (role.permissions.contains("*") && user.role?.permissions?.contains("*") == false) {
            context.status(403).json(message("You cannot assign the admin role"))
            return
        }

        user.role = role

        RestModule.instance.userProvider.edit(user)
        context.status(201).json(message("User updated"))
    }

    @Request(requestType = RequestType.PATCH, path = "/self/change-password", "polocloud.user.self.change-password")
    fun changePassword(context: Context, user: User) {
        val userPasswordChangeModel = try {
            context.bodyAsClass(UserPasswordChangeModel::class.java)
        } catch (e: Exception) {
            context.status(400).json(message("Invalid body"))
            return
        }

        if (userPasswordChangeModel.password.isBlank()) {
            context.status(400).json(message("New password cannot be empty"))
            return
        }

        user.passwordHash = EncryptionUtil.encrypt(userPasswordChangeModel.password)
        user.hasChangedPassword = true

        RestModule.instance.userProvider.edit(user)
        context.status(200).json(message("Password changed successfully"))
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
            }.toString()
        )
    }

    @Request(requestType = RequestType.GET, path = "/self")
    fun self(context: Context, user: User) {
        context.status(200).json(
            JsonObject().apply {
                addProperty("uuid", user.uuid.toString())
                addProperty("username", user.username)
                addProperty("hasChangedPassword", user.hasChangedPassword)
                addProperty("createdAt", user.createdAt)
                addProperty("role", user.role?.id ?: 0)
            }.toString()
        )
    }

    @Request(requestType = RequestType.GET, path = "/tokens", permission = "polocloud.user.self.tokens")
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

    @Request(requestType = RequestType.DELETE, path = "/token/{token}", permission = "polocloud.user.self.token.delete")
    fun deleteToken(context: Context, user: User, token: Token) {
        val tokenValue = context.pathParam("token")
        val deletionToken = user.tokens.firstOrNull { it.value == tokenValue }

        if (deletionToken == null) {
            context.status(404).json(message("Token not found"))
            return
        }

        if (deletionToken == token) {
            context.removeCookie("token")
        }

        RestModule.instance.userProvider.deleteToken(user, deletionToken)
        context.status(204).json(message("Token deleted"))
    }

    @Request(requestType = RequestType.DELETE, path = "/tokens", permission = "polocloud.user.self.token.delete")
    fun deleteAllTokens(context: Context, user: User) {
        context.removeCookie("token")
        RestModule.instance.userProvider.deleteAllTokens(user)
        context.status(204).json(message("All tokens deleted"))
    }
}