package dev.httpmarco.polocloud.addons.proxy

import dev.httpmarco.polocloud.addons.proxy.tablist.Tablist

class ProxyConfigAccessor(val config: ProxyConfiguration): ProxyConfig {
    override fun prefix(): String {
        return this.config.prefix
    }

    override fun messages(key: String): String {
        return this.config.messages[key] ?: "Message $key not found"
    }

    override fun tablist(): Tablist {
        return this.config.tablist
    }
}