package dev.httpmarco.polocloud.modules.rest.socket.web.impl.v3

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.modules.rest.socket.BaseWebSocket
import dev.httpmarco.polocloud.modules.rest.socket.SocketSender
import dev.httpmarco.polocloud.shared.events.definitions.PolocloudLogEvent
import io.javalin.http.Context
import io.javalin.websocket.WsCloseContext
import io.javalin.websocket.WsConnectContext
import io.javalin.websocket.WsMessageContext

class PolocloudLogWebSocket : BaseWebSocket("/logs", "polocloud.ws.logs"), SocketSender {

    override fun onConnect(context: WsConnectContext) {
        this.clients += context

        Agent.eventProvider().subscribe(PolocloudLogEvent::class.java, { event ->
            if (context.session.isOpen) {
                send(event.log)
            }
        })

        context.enableAutomaticPings()
    }

    override fun onClose(context: WsCloseContext) {
        this.clients -= context
        //TODO remove subscriptions
    }

    override fun onMessage(context: WsMessageContext) {

    }

    override fun onError(context: Context) {

    }

    override fun send(message: String) {
        this.clients.forEach { client ->
            if (client.session.isOpen) {
                client.send(message)
            }
        }
    }
}