package dev.httpmarco.polocloud.modules.rest.socket.web.impl.v3.service

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.services.AbstractService
import dev.httpmarco.polocloud.modules.rest.socket.BaseWebSocket
import dev.httpmarco.polocloud.modules.rest.socket.SocketSender
import dev.httpmarco.polocloud.shared.events.definitions.ServiceLogEvent
import io.javalin.http.Context
import io.javalin.websocket.WsCloseContext
import io.javalin.websocket.WsConnectContext
import io.javalin.websocket.WsMessageContext

class ServiceScreenWebSocket : BaseWebSocket("/service/{serviceName}/screen", "polocloud.service.screen"), SocketSender {

    override fun onConnect(context: WsConnectContext) {
        this.clients += context

        val serviceName = context.pathParam("serviceName")
        val service = Agent.serviceProvider().find(serviceName)
        if (service == null) {
            context.closeSession(1008, "Service not found")
            return
        }

        (service as AbstractService).logs(5000).forEach {
            context.send(it)
        }

        Agent.eventProvider().subscribe(ServiceLogEvent::class.java, { event ->
            if (event.service == service && context.session.isOpen) {
                context.send(event.line)
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