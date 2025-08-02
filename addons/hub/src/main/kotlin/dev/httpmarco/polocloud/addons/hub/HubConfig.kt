package dev.httpmarco.polocloud.addons.hub

interface HubConfig {
    fun messages(key: String): String
    fun aliases(): List<String>
}