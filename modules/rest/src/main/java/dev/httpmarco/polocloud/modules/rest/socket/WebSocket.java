package dev.httpmarco.polocloud.modules.rest.socket;

import dev.httpmarco.polocloud.modules.rest.RestModule;
import io.javalin.websocket.*;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public abstract class WebSocket implements SocketSendable {

    private final String path;
    private final String permission;
    protected final RestModule restModule;
    private final List<WsContext> connectedClients;

    public WebSocket(String path, String permission, RestModule restModule) {
        this.path = path;
        this.permission = permission;
        this.restModule = restModule;

        this.connectedClients = new ArrayList<>();
    }

    public abstract void onConnect(WsConnectContext context);

    public abstract void onClose(WsCloseContext context);

    public abstract void onMessage(WsMessageContext context);

    public abstract void onError(WsErrorContext context);

    @Override
    public void send(String content) {
        for (var client : this.connectedClients) {
            if (!client.session.isOpen()) {
                this.connectedClients.remove(client);
                continue;
            }

            client.send(content);
        }
    }
}
