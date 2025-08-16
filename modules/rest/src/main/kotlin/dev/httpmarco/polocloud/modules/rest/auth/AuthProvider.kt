package dev.httpmarco.polocloud.modules.rest.auth

import dev.httpmarco.polocloud.modules.rest.RestModule
import dev.httpmarco.polocloud.modules.rest.auth.user.User
import dev.httpmarco.polocloud.modules.rest.controller.ControllerProvider.Companion.API_PATH
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestMethodData
import dev.httpmarco.polocloud.modules.rest.usersConfiguration
import io.javalin.http.Context
import io.javalin.http.HandlerType
import java.util.UUID

class AuthProvider(
    private val requestMethodData: RequestMethodData
) {

    fun handle(context: Context) {
        var user: User? = null
        var token: String? = null

        if (!isUserCreationAllowed(context)) {
            if (!isLogin(context) && !isAlive(context)) {
                user = userByContext(context)

                if (user == null) {
                    context.status(401).result("Unauthorized")
                    return
                }

                token = context.cookie("token")

                if (token !in user.tokens) {
                    context.status(401).result("Invalid or expired token")
                    return
                }

                if (!isPermitted(user)) {
                    context.status(403).result("Forbidden")
                    return
                }
            }
        }

        RestModule.instance.controllerProvider.processRequest(
            requestMethodData.method,
            requestMethodData.controller,
            context,
            user,
            token
        )
    }

    private fun isLogin(context: Context): Boolean {
        return context.path().trimEnd('/') == "$API_PATH/auth/login"
                && context.method() == HandlerType.POST
                && usersConfiguration.users.isNotEmpty()
    }

    private fun isAlive(context: Context): Boolean {
        return context.path().trimEnd('/') == "$API_PATH/alive"
                && context.method() == HandlerType.GET
    }

    private fun isUserCreationAllowed(context: Context): Boolean {
        return context.path().trimEnd('/') == "$API_PATH/user"
                && context.method() == HandlerType.POST
                && usersConfiguration.users.isEmpty()
    }

    private fun userByContext(context: Context): User? {
        val decodedToken = context.cookie("token")?.let {
            RestModule.instance.jwtProvider.provider().validateToken(it)
        }

        if (decodedToken == null) {
            context.status(401).result("Missing or invalid token")
            return null
        }

        val uuid = decodedToken.get().getClaim("uuid").asString()?.let {
            try { UUID.fromString(it) } catch (e: IllegalArgumentException) { null }
        }

        if (uuid == null) {
            context.status(401).result("Invalid token: missing UUID claim")
            return null
        }

        return RestModule.instance.userProvider.userByUUID(uuid)
    }

    private fun isPermitted(user: User): Boolean {
        val permission = requestMethodData.permission
        return permission.isEmpty() || user.hasPermission(permission)
    }
}