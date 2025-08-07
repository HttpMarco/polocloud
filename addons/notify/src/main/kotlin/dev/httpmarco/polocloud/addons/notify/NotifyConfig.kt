package dev.httpmarco.polocloud.addons.notify

interface NotifyConfig {
    fun prefix(): String
    fun messages(key: String): String
}