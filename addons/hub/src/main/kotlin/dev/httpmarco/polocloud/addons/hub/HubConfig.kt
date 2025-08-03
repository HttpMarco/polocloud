package dev.httpmarco.polocloud.addons.hub

interface HubConfig {
    fun prefix(): String
    fun messages(key: String): String
    fun aliases(): List<String>
}