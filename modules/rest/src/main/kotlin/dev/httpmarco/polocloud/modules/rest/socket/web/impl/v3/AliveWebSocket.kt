package dev.httpmarco.polocloud.modules.rest.socket.web.impl.v3

import dev.httpmarco.polocloud.modules.rest.socket.BaseWebSocket
import io.javalin.http.Context
import io.javalin.websocket.WsCloseContext
import io.javalin.websocket.WsConnectContext
import io.javalin.websocket.WsMessageContext

class AliveWebSocket : BaseWebSocket("/alive", "polocloud.ws.alive") {

    override fun onConnect(context: WsConnectContext) {
        this.clients += context
        context.enableAutomaticPings()
    }

    override fun onClose(context: WsCloseContext) {
        this.clients -= context
    }

    override fun onMessage(context: WsMessageContext) {

    }

    override fun onError(context: Context) {
        //
    }
}