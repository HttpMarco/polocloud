package dev.httpmarco.polocloud.addons.api

interface MessageFormatter {
    fun formatPrefix(): String
    fun format(message: String): String
    fun formatTablistHeader(): String
    fun formatTablistFooter(): String
    fun formatMotdLineOne(): String
    fun formatMotdLineTwo(): String
    fun formatMaintenanceMotdLineOne(): String
    fun formatMaintenanceMotdLineTwo(): String
    fun formatMaintenancePingMessage(): String
}