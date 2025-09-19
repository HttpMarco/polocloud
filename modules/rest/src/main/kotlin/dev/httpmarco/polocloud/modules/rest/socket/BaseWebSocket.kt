package dev.httpmarco.polocloud.modules.rest.socket

import io.javalin.http.Context
import io.javalin.websocket.WsCloseContext
import io.javalin.websocket.WsConnectContext
import io.javalin.websocket.WsContext
import io.javalin.websocket.WsMessageContext

abstract class BaseWebSocket(
    val path: String,
    val requiredPermission: String
) {

    val clients = mutableSetOf<WsContext>()

    abstract fun onConnect(context: WsConnectContext)
    abstract fun onClose(context: WsCloseContext)
    abstract fun onMessage(context: WsMessageContext)
    abstract fun onError(context: Context)

    fun shutdown() {
        clients.forEach { client ->
            if (client.session.isOpen) {
                client.session.close()
            }
        }
        clients.clear()
    }
}