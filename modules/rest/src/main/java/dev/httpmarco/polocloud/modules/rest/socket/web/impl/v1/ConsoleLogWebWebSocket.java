package dev.httpmarco.polocloud.modules.rest.socket.web.impl.v1;

import dev.httpmarco.polocloud.api.event.impl.services.log.ServiceLogEvent;
import dev.httpmarco.polocloud.modules.rest.RestModule;
import dev.httpmarco.polocloud.modules.rest.socket.WebSocket;
import dev.httpmarco.polocloud.node.Node;
import io.javalin.websocket.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ConsoleLogWebWebSocket extends WebSocket {


    public ConsoleLogWebWebSocket(RestModule restModule) {
        super("/{service}/log", "polocloud.service.screen", restModule);
    }

    @Override
    public void onConnect(WsConnectContext context) {
        var serviceName = context.pathParam("service");
        var service = Node.instance().serviceProvider().find(serviceName);

        if (service == null) {
            context.session.close(1008, "Service not found");
            return;
        }

        // send existing logs
        for (var log : service.logs()) {
            context.send(log);
        }

        // send new logs
        Node.instance().eventProvider().listen(ServiceLogEvent.class, event -> {
            if (event.service().id().equals(service.id())) {
                for (var log : event.newLogs()) {
                    context.send(log);
                }
            }
        });

        connectedClients().add(context);
        context.enableAutomaticPings();
    }

    @Override
    public void onClose(WsCloseContext context) {
        connectedClients().remove(context);
    }

    @Override
    public void onMessage(WsMessageContext context) {

    }

    @Override
    public void onError(WsErrorContext context) {

    }
}
