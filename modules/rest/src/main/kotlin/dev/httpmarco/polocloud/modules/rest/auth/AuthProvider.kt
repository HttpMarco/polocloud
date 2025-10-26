package dev.httpmarco.polocloud.modules.rest.auth

import dev.httpmarco.polocloud.modules.rest.RestModule
import dev.httpmarco.polocloud.modules.rest.auth.user.User
import dev.httpmarco.polocloud.modules.rest.auth.user.token.Token
import dev.httpmarco.polocloud.modules.rest.controller.ControllerProvider.Companion.API_PATH
import dev.httpmarco.polocloud.modules.rest.controller.OpenEndpoint
import dev.httpmarco.polocloud.modules.rest.controller.defaultResponse
import dev.httpmarco.polocloud.modules.rest.controller.methods.RequestMethodData
import dev.httpmarco.polocloud.modules.rest.usersConfiguration
import io.javalin.http.Context
import io.javalin.http.HandlerType
import java.util.UUID

class AuthProvider(
    private val requestData: RequestMethodData
) {

    private val openEndpoints = listOf(
        OpenEndpoint("$API_PATH/health", HandlerType.GET), // health endpoint needs to be public to check if the restapi is reachable
        OpenEndpoint("$API_PATH/auth/login", HandlerType.POST) { usersConfiguration.users.isNotEmpty() }, // normal login
        OpenEndpoint("$API_PATH/user/self", HandlerType.POST) { usersConfiguration.users.isEmpty() } // Admin account setup (first account creation)
    )

    fun handle(context: Context) {
        if (isOpenEndpoint(context)) {
            process(context, null, null)
            return
        }

        val tokenValue = context.cookie("token")
        val user = validateTokenAndFetchUser(context, tokenValue) ?: return

        if (!user.tokens.any { it.value == tokenValue }) {
            context.defaultResponse(401,"Invalid or expired token")
            return
        }

        if (!hasPermission(user)) {
            context.defaultResponse(403,"Forbidden")
            return
        }

        val token = user.tokens.first { it.value == tokenValue }
        RestModule.instance.userProvider.updateActivity(user, token)

        process(context, user, token)
    }

    private fun isOpenEndpoint(context: Context): Boolean {
        val path = context.path().trimEnd('/')
        val method = context.method()

        return openEndpoints.any { endpoint ->
            endpoint.path == path &&
                    endpoint.method == method &&
                    endpoint.condition()
        }
    }

    private fun process(context: Context, user: User?, token: Token?) {
        RestModule.instance.controllerProvider.processRequest(
            requestData.method,
            requestData.controller,
            context,
            user,
            token
        )
    }

    private fun validateTokenAndFetchUser(context: Context, token: String?): User? {
        if (token.isNullOrBlank()) {
            context.defaultResponse(401,"Missing token")
            return null
        }

        val jwt = RestModule.instance.jwtProvider.provider()
            .validateToken(token)
            .takeIf { it?.isPresent == true }
            ?.get()
            ?: run {
                context.defaultResponse(401,"Invalid token")
                return null
            }

        val uuid = try {
            UUID.fromString(jwt.getClaim("uuid").asString())
        } catch (_: IllegalArgumentException) {
            null
        } ?: run {
            context.defaultResponse(401,"Invalid token: missing UUID claim")
            return null
        }

        return RestModule.instance.userProvider.userByUUID(uuid)
    }

    private fun hasPermission(user: User): Boolean {
        val permission = requestData.permission
        return permission.isEmpty() || user.role?.hasPermission(permission) == true
    }
}