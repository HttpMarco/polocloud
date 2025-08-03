package dev.httpmarco.polocloud.addons.proxy

import dev.httpmarco.polocloud.addons.proxy.tablist.Tablist

interface ProxyConfig {
    fun prefix(): String
    fun messages(key: String): String
    fun tablist(): Tablist
}