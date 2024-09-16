package dev.httpmarco.polocloud.modules.rest.socket.web.impl.v1;

import dev.httpmarco.polocloud.modules.rest.RestModule;
import dev.httpmarco.polocloud.modules.rest.socket.WebSocket;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsErrorContext;
import io.javalin.websocket.WsMessageContext;


public class ConsoleLogWebWebSocket extends WebSocket {

    public ConsoleLogWebWebSocket(RestModule restModule) {
        super("/log", restModule);
    }

    @Override
    public void onConnect(WsConnectContext context) {

    }

    @Override
    public void onClose(WsCloseContext context) {

    }

    @Override
    public void onMessage(WsMessageContext context) {

    }

    @Override
    public void onError(WsErrorContext context) {

    }

}
