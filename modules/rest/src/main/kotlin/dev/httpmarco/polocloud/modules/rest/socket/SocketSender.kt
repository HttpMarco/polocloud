package dev.httpmarco.polocloud.modules.rest.socket

fun interface SocketSender {
    fun send(message: String)
}