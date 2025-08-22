package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller

import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.RestModule
import dev.httpmarco.polocloud.modules.rest.auth.user.token.Token
import dev.httpmarco.polocloud.modules.rest.auth.user.User
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.AuthModel
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import io.javalin.http.Context
import io.javalin.http.Cookie
import java.util.concurrent.TimeUnit

class AuthController : Controller("/auth") {

    @Request(requestType = RequestType.POST, path = "/login")
    fun login(context: Context) {
        val authModel = try {
            context.bodyAsClass(AuthModel::class.java)
        } catch (e: Exception) {
            context.status(400).json(message("Invalid body"))
            return
        }

        if (authModel.username.isBlank() || authModel.password.isBlank()) {
            context.status(400).json(message("Invalid body: missing fields"))
            return
        }

        val token = RestModule.Companion.instance.userProvider.login(authModel.username, authModel.password, context.ip(), context.userAgent())
        if (token == null) {
            context.status(401).json(message("Invalid credentials"))
            return
        }

        val cookie = Cookie(
            "token",
            token.value,
            maxAge = TimeUnit.DAYS.toSeconds(7).toInt(),
            isHttpOnly = true,
            secure = true,
        )

        context.status(200).cookie(cookie).json(message("Login successful"))
    }

    @Request(requestType = RequestType.POST, path = "/logout")
    fun logout(context: Context, user: User, token: Token) {
        RestModule.instance.userProvider.logout(user, token)

        context.removeCookie("token")
        context.status(200).json(message("Logout successful"))
    }

    @Request(requestType = RequestType.GET, path = "/token")
    fun checkToken(context: Context) {
        val token = context.cookie("token")

        if (token == null) {
            context.status(401).json(message("No token found"))
            return
        }

        context.status(200).json(JsonObject().apply {
            addProperty("token", token)
            addProperty("message", "Token is valid")
        }.toString())
    }
}