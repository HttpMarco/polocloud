package dev.httpmarco.polocloud.modules.rest.socket.web.impl.v3.service

import com.google.gson.JsonObject
import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.modules.rest.socket.BaseWebSocket
import dev.httpmarco.polocloud.modules.rest.socket.SocketSender
import dev.httpmarco.polocloud.shared.events.definitions.service.ServiceChangeStateEvent
import dev.httpmarco.polocloud.shared.service.Service
import io.javalin.http.Context
import io.javalin.websocket.WsCloseContext
import io.javalin.websocket.WsConnectContext
import io.javalin.websocket.WsMessageContext

class EventWebSocket : BaseWebSocket("/services/update", "polocloud.ws.service.update"), SocketSender {
    override fun onConnect(context: WsConnectContext) {
        this.clients += context

        Agent.eventProvider().subscribe(ServiceChangeStateEvent::class.java) { event ->
            if (context.session.isOpen) {
                update(event.service)
            }
        }

        context.enableAutomaticPings()
    }

    override fun onClose(context: WsCloseContext) {
        this.clients -= context
    }

    override fun onMessage(context: WsMessageContext) {

    }

    override fun onError(context: Context) {

    }

    fun update(service: Service) {
        val message = JsonObject().apply {
            addProperty("serviceName", service.name())
            addProperty("state", service.state.name)
            addProperty("timestamp", System.currentTimeMillis().toString())
        }

        send(message.toString())
    }

    override fun send(message: String) {
        this.clients.forEach { client ->
            if (client.session.isOpen) {
                client.send(message)
            }
        }
    }
}