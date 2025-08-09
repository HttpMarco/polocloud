package dev.httpmarco.polocloud.addons.api

interface MessageFormatter {
    fun formatPrefix(): String
    fun format(message: String): String
    fun tablistEnabled(): Boolean
    fun formatTablistHeader(): String
    fun formatTablistFooter(): String
    fun motdEnabled(): Boolean
    fun formatMotdLineOne(): String
    fun formatMotdLineTwo(): String
    fun formatMaintenanceMotdLineOne(): String
    fun formatMaintenanceMotdLineTwo(): String
}