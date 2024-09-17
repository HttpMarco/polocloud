package dev.httpmarco.polocloud.modules.rest.socket.web.impl.v1;

import dev.httpmarco.polocloud.modules.rest.RestModule;
import dev.httpmarco.polocloud.modules.rest.socket.WebSocket;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsErrorContext;
import io.javalin.websocket.WsMessageContext;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ConsoleLogWebWebSocket extends WebSocket {

    public ConsoleLogWebWebSocket(RestModule restModule) {
        super("/log", "", restModule);
    }

    @Override
    public void onConnect(WsConnectContext context) {
        context.send("Connected to the console log websocket!");
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
