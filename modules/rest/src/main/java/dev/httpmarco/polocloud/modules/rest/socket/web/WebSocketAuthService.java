package dev.httpmarco.polocloud.modules.rest.socket.web;

import dev.httpmarco.polocloud.modules.rest.RestModule;
import dev.httpmarco.polocloud.modules.rest.socket.WebSocket;
import io.javalin.websocket.WsConnectContext;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class WebSocketAuthService {

    private final RestModule restModule;

    public void authenticate(WebSocket webSocket, WsConnectContext context) {
        var decodedToken = tokenFromWebSocketHeader(context).flatMap(this.restModule.jwtProvider().provider()::validateToken);
        if (decodedToken.isEmpty()) {
            context.closeSession(3000, "Unauthorized");
            return;
        }

        var token = decodedToken.get();
        var uuid = token.getClaim("uuid").asString();

        var user = this.restModule.userService().userByUUID(UUID.fromString(uuid));
        if (user == null) {
            context.closeSession(3000, "Unauthorized");
            return;
        }

        if (user.permissions().isEmpty() || !user.hasPermission(webSocket.permission())) {
            context.closeSession(3003, "Forbidden");
            return;
        }

        // calling the connect method
        webSocket.onConnect(context);
    }

    private Optional<String> tokenFromWebSocketHeader(WsConnectContext context) {
        return Optional.ofNullable(context.cookie("token"));
    }
}
