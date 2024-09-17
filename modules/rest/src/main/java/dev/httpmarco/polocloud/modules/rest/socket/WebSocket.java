package dev.httpmarco.polocloud.modules.rest.socket;

import dev.httpmarco.polocloud.modules.rest.RestModule;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsErrorContext;
import io.javalin.websocket.WsMessageContext;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public abstract class WebSocket {

    private final String path;
    private final String permission;
    protected final RestModule restModule;

    public WebSocket(String path, String permission, RestModule restModule) {
        this.path = path;
        this.permission = permission;
        this.restModule = restModule;
    }

    public abstract void onConnect(WsConnectContext context);

    public abstract void onClose(WsCloseContext context);

    public abstract void onMessage(WsMessageContext context);

    public abstract void onError(WsErrorContext context);
}
