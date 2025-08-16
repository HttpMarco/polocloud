package dev.httpmarco.polocloud.shared.logging

interface Logger {

    fun info(message: String)
    fun warn(message: String)
    fun error(message: String)
    fun debug(message: String)

}