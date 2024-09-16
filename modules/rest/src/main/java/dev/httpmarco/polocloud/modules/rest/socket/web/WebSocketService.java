package dev.httpmarco.polocloud.modules.rest.socket.web;

import dev.httpmarco.polocloud.modules.rest.RestModule;
import dev.httpmarco.polocloud.modules.rest.socket.SocketService;
import dev.httpmarco.polocloud.modules.rest.socket.web.impl.v1.ConsoleLogWebSocket;

import java.util.ArrayList;
import java.util.List;


public class WebSocketService {

    public static final String API_PATH = "/polocloud/api/v1";
    private final RestModule restModule;
    private final List<SocketService> webSockets;

    public WebSocketService(RestModule restModule) {
        this.restModule = restModule;
        this.webSockets = new ArrayList<>();

        invoke();
    }

    private void registerWebSockets(SocketService... webSocket) {
        this.webSockets.addAll(List.of(webSocket));
    }

    public void invoke() {
        registerWebSockets(
                new ConsoleLogWebSocket(this.restModule)
        );
        //TODO auth
        for (var webSocket : this.webSockets) {
            this.restModule.app().ws(API_PATH + webSocket.path(), ws -> {
                ws.onConnect(webSocket::onConnect);
                ws.onClose(webSocket::onClose);
                ws.onMessage(webSocket::onMessage);
                ws.onError(webSocket::onError);
            });
        }
    }
}
