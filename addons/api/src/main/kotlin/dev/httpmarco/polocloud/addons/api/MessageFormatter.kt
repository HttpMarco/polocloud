package dev.httpmarco.polocloud.addons.api

interface MessageFormatter {
    fun formatPrefix(): String
    fun format(message: String): String
    fun formatTablistHeader(): String
    fun formatTablistFooter(): String
}