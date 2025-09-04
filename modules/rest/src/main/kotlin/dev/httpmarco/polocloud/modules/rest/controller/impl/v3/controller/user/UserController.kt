package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.user

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.RestModule
import dev.httpmarco.polocloud.modules.rest.auth.EncryptionUtil
import dev.httpmarco.polocloud.modules.rest.auth.user.User
import dev.httpmarco.polocloud.modules.rest.auth.user.toJson
import dev.httpmarco.polocloud.modules.rest.auth.user.token.Token
import dev.httpmarco.polocloud.modules.rest.auth.user.token.toJson
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.defaultResponse
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
        val model = context.parseBodyOrBadRequest<UserCreateModel>() ?: return
        if (!context.validate(model.username.isNotBlank(), "Username is required")) return

        val role = context.validateRole(model.roleId) ?: return
        val password = RestModule.instance.userProvider.create(model.username, role)

        val data = JsonObject().apply {
            addProperty("password", password)
        }

        context.defaultResponse(201, "User created successfully", data)
    }

    @Request(requestType = RequestType.POST, path = "/self")
    fun createSelfUser(context: Context) {
        val model = context.parseBodyOrBadRequest<UserSelfCreateModel>() ?: return
        if (!context.validate(model.username.isNotBlank(), "Username is required")) return
        if (!context.validate(model.password.isNotBlank(), "Password is required")) return

        if (RestModule.instance.userProvider.users().isNotEmpty()) {
            context.defaultResponse(400, "A user already exists")
            return
        }

        val role = context.validateRole(model.roleId) ?: return

        val hashedPassword = EncryptionUtil.encrypt(model.password)
        val user = User(UUID.randomUUID(), model.username, role, hashedPassword, true, System.currentTimeMillis())

        val token = RestModule.instance.userProvider.createSelf(user, context.ip(), context.userAgent())
        if (token == null) {
            context.defaultResponse(400, "User already exists")
            return
        }

        val cookie = Cookie(
            "token",
            token.value,
            maxAge = TimeUnit.DAYS.toSeconds(7).toInt(),
            isHttpOnly = true,
            secure = true,
        )

        context.cookie(cookie).defaultResponse(201,"User created successfully")
    }

    @Request(requestType = RequestType.PATCH, path = "/self/edit", permission = "polocloud.user.self.edit")
    fun selfEdit(context: Context, user: User) {
        val model = context.parseBodyOrBadRequest<UserSelfEditModel>() ?: return
        if (!context.validate(model.username.isNotBlank(), "Username is required")) return

        user.username = model.username

        RestModule.instance.userProvider.edit(user)
        context.defaultResponse(201,"User updated")
    }

    @Request(requestType = RequestType.PATCH, path = "/edit", permission = "polocloud.user.edit")
    fun edit(context: Context) {
        val model = context.parseBodyOrBadRequest<UserEditModel>() ?: return
        if (!context.validate(model.uuid.isNotBlank(), "UUID is required")) return

        val userUUID = UUID.fromString(model.uuid)
        val user = RestModule.instance.userProvider.userByUUID(userUUID)
        if (user == null) {
            context.defaultResponse(404, "User not found")
            return
        }

        val role = context.validateRole(model.roleId) ?: return
        if (role.permissions.contains("*") && user.role?.permissions?.contains("*") == true) {
            context.defaultResponse(403,"You cannot assign the admin role")
            return
        }

        user.role = role

        RestModule.instance.userProvider.edit(user)
        context.defaultResponse(201, "User updated")
    }

    @Request(requestType = RequestType.PATCH, path = "/self/change-password", "polocloud.user.self.change-password")
    fun changePassword(context: Context, user: User) {
        val model = context.parseBodyOrBadRequest<UserPasswordChangeModel>() ?: return
        if (!context.validate(model.password.isNotBlank(), "Password is required")) return

        user.passwordHash = EncryptionUtil.encrypt(model.password)
        user.hasChangedPassword = true

        RestModule.instance.userProvider.edit(user)
        context.defaultResponse(200, "Password changed successfully")
    }

    @Request(requestType = RequestType.DELETE, path = "/{uuid}", permission = "polocloud.user.delete")
    fun delete(context: Context) {
        val userUUID = context.parseUUID("uuid") ?: return

        RestModule.instance.userProvider.delete(userUUID)
        context.defaultResponse(204)
    }

    @Request(requestType = RequestType.DELETE, path = "/self")
    fun deleteSelf(context: Context, user: User) {
        RestModule.instance.userProvider.delete(user.uuid)
        context.removeCookie("token")
        context.defaultResponse(204)
    }

    @Request(requestType = RequestType.GET, path = "s/", permission = "polocloud.user.list")
    fun list(context: Context) {
        context.defaultResponse(200, data = JsonArray().apply { RestModule.instance.userProvider.users().forEach { add(it.toJson()) } })
    }

    @Request(requestType = RequestType.GET, path = "/{uuid}", permission = "polocloud.user.get")
    fun getUser(context: Context) {
        val userUUID = context.parseUUID("uuid") ?: return

        val user = RestModule.instance.userProvider.userByUUID(userUUID)
        if (user == null) {
            context.defaultResponse(404, "User not found")
            return
        }

        context.defaultResponse(200, data = user.toJson())
    }

    @Request(requestType = RequestType.GET, path = "/self")
    fun self(context: Context, user: User) {
        context.defaultResponse(200, data = user.toJson())
    }

    @Request(requestType = RequestType.GET, path = "/tokens", permission = "polocloud.user.self.tokens")
    fun tokens(context: Context, user: User) {
        context.defaultResponse(200, data = JsonArray().apply { user.tokens.forEach { token -> add(token.toJson()) } })
    }

    @Request(requestType = RequestType.DELETE, path = "/token/{token}", permission = "polocloud.user.self.token.delete")
    fun deleteToken(context: Context, user: User, token: Token) {
        val tokenValue = context.pathParam("token")
        val deletionToken = user.tokens.firstOrNull { it.value == tokenValue }

        if (deletionToken == null) {
            context.defaultResponse(404, "Token not found")
            return
        }

        if (deletionToken == token) {
            context.removeCookie("token")
        }

        RestModule.instance.userProvider.deleteToken(user, deletionToken)
        context.defaultResponse(204, "Token deleted")
    }

    @Request(requestType = RequestType.DELETE, path = "/tokens", permission = "polocloud.user.self.token.delete")
    fun deleteAllTokens(context: Context, user: User) {
        context.removeCookie("token")
        RestModule.instance.userProvider.deleteAllTokens(user)
        context.defaultResponse(204, "All tokens deleted")
    }
}