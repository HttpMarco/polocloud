package dev.httpmarco.polocloud.modules.rest.socket.web

import dev.httpmarco.polocloud.modules.rest.RestModule
import dev.httpmarco.polocloud.modules.rest.socket.BaseWebSocket
import io.javalin.websocket.WsConnectContext
import java.util.Optional
import java.util.UUID

class WebSocketAuthService {

    fun authenticate(socket: BaseWebSocket, context: WsConnectContext) {
        val decodedToken = extractToken(context).flatMap { RestModule.instance.jwtProvider.provider().validateToken(it) }
        if (decodedToken.isEmpty) {
            context.closeSession(3000, "Unauthorized")
            return
        }

        val uuid = decodedToken.get().getClaim("uuid").asString()
        val user = RestModule.instance.userProvider.userByUUID(UUID.fromString(uuid))

        if (user == null) {
            context.closeSession(3000, "Unauthorized")
            return
        }

        if (user.role == null) {
            context.closeSession(3000, "Unauthorized")
            return
        }

        when {
            user.role!!.permissions.isEmpty() || user.role!!.hasPermission(socket.requiredPermission) -> context.closeSession(3003, "Forbidden")
            else -> socket.onConnect(context)
        }
    }

    private fun extractToken(context: WsConnectContext): Optional<String> = Optional.ofNullable(context.cookie("token"))

}