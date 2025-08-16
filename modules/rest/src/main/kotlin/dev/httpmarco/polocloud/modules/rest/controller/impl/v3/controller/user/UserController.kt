package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller.user

import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.RestModule
import dev.httpmarco.polocloud.modules.rest.auth.EncryptionUtil
import dev.httpmarco.polocloud.modules.rest.auth.user.User
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.user.UserModel
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import io.javalin.http.Context
import io.javalin.http.Cookie
import java.util.UUID
import java.util.concurrent.TimeUnit

class UserController : Controller("/user") {

    @Request(requestType = RequestType.POST, path = "/")
    fun createUser(context: Context) {
        val userModel = try {
            context.bodyAsClass(UserModel::class.java)
        } catch (e: Exception) {
            context.status(400).result("Invalid body")
            return
        }

        if (userModel.username.isBlank() || userModel.password.isBlank()) {
            context.status(400).json(message("Invalid body: missing fields"))
            return
        }

        val hashedPassword = EncryptionUtil.encrypt(userModel.password)
        val user = User(UUID.randomUUID(), userModel.username, hashedPassword, System.currentTimeMillis())

        val token = RestModule.Companion.instance.userProvider.create(user, context.ip(), context.userAgent())
        if (token == null) {
            context.status(400).json(message("User already exists"))
            return
        }

        val cookie = Cookie(
            "token",
            token,
            maxAge = TimeUnit.DAYS.toSeconds(7).toInt(),
            isHttpOnly = true,
            secure = true,
        )

        context.status(201).cookie(cookie).json(message("User created"))
    }

    @Request(requestType = RequestType.GET, path = "/self")
    fun self(context: Context, user: User) {
        context.status(200).json(
            JsonObject().apply {
                addProperty("uuid", user.uuid.toString())
                addProperty("username", user.username)
                addProperty("createdAt", user.createdAt)
            }.toString()
        )
    }
}