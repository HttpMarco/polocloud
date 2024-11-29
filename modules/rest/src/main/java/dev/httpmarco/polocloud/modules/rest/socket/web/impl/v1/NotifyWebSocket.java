package dev.httpmarco.polocloud.modules.rest.socket.web.impl.v1;

import com.google.gson.Gson;
import dev.httpmarco.polocloud.api.event.impl.services.ServiceStartEvent;
import dev.httpmarco.polocloud.api.event.impl.services.ServiceStoppingEvent;
import dev.httpmarco.polocloud.modules.rest.RestModule;
import dev.httpmarco.polocloud.modules.rest.socket.WebSocket;
import dev.httpmarco.polocloud.node.Node;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsErrorContext;
import io.javalin.websocket.WsMessageContext;

import java.util.Map;

public class NotifyWebSocket extends WebSocket {

    public NotifyWebSocket(RestModule restModule) {
        super("/notify", "polocloud.notify", restModule);
    }

    @Override
    public void onConnect(WsConnectContext context) {

        /*
         *  We need this for every notification type
         *  Node.instance().eventProvider().listen(.class, event -> {
         *
         *  });
         */

        /*
         * Whe notify the client when a new service starts
         */
        Node.instance().eventProvider().listen(ServiceStartEvent.class, event -> {
            var service = event.service();

            sendNotification("service-start", Map.of(
                    "id", service.id(),
                    "name", service.name()
            ));
        });

        /*
         * Whe notify the client when a new service stops
         */
        Node.instance().eventProvider().listen(ServiceStoppingEvent.class, event -> {
            var service = event.service();

            sendNotification("service-stop", Map.of(
                    "id", service.id(),
                    "name", service.name()
            ));
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

    public void sendNotification(String type, Object data) {
        var message = Map.of(
          "type", type,
          "data", data
        );

        send(new Gson().toJson(message));
    }
}
