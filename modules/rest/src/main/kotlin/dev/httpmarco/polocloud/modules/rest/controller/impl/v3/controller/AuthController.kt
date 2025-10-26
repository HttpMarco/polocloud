package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.controller

import com.google.gson.JsonObject
import dev.httpmarco.polocloud.modules.rest.RestModule
import dev.httpmarco.polocloud.modules.rest.auth.user.token.Token
import dev.httpmarco.polocloud.modules.rest.auth.user.User
import dev.httpmarco.polocloud.modules.rest.controller.Controller
import dev.httpmarco.polocloud.modules.rest.controller.defaultResponse
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.AuthModel
import dev.httpmarco.polocloud.modules.rest.controller.methods.Request
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestType
import io.javalin.http.Context
import io.javalin.http.Cookie
import java.util.concurrent.TimeUnit

class AuthController : Controller("/auth") {

    @Request(requestType = RequestType.POST, path = "/login")
    fun login(context: Context) {
        val model = context.parseBodyOrBadRequest<AuthModel>() ?: return
        if (!context.validate(model.username.isNotBlank(), "Username is required")) return
        if (!context.validate(model.password.isNotBlank(), "Password is required")) return

        val token = RestModule.Companion.instance.userProvider.login(model.username, model.password, context.ip(), context.userAgent())
        if (token == null) {
            context.defaultResponse(401, "Invalid credentials")
            return
        }

        val cookie = Cookie(
            "token",
            token.value,
            maxAge = TimeUnit.DAYS.toSeconds(7).toInt(),
            isHttpOnly = true,
            secure = true,
        )

        context.cookie(cookie).defaultResponse(200, "Login successful")
    }

    @Request(requestType = RequestType.POST, path = "/logout")
    fun logout(context: Context, user: User, token: Token) {
        RestModule.instance.userProvider.logout(user, token)

        context.removeCookie("token")
        context.defaultResponse(200, "Logout successful")
    }

    @Request(requestType = RequestType.GET, path = "/token")
    fun checkToken(context: Context) {
        val token = context.cookie("token")

        if (token == null) {
            context.defaultResponse(401,"No token found")
            return
        }

        val data = JsonObject().apply {
            addProperty("token", token)
        }

        context.defaultResponse(200, "Token is valid", data)
    }
}