package dev.httpmarco.polocloud.addons.proxy

import dev.httpmarco.polocloud.addons.proxy.motd.Motd
import dev.httpmarco.polocloud.addons.proxy.tablist.Tablist

interface ProxyConfig {
    fun prefix(): String
    fun messages(key: String): String
    fun aliases(): List<String>
    fun tablist(): Tablist
    fun motd(): Motd
    fun maintenanceMotd(): Motd
}