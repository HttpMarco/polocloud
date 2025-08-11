package dev.httpmarco.polocloud.addons.proxy

import dev.httpmarco.polocloud.addons.proxy.motd.Motd
import dev.httpmarco.polocloud.addons.proxy.tablist.Tablist

class ProxyConfigAccessor(val config: ProxyConfiguration): ProxyConfig {
    override fun prefix(): String {
        return this.config.prefix
    }

    override fun messages(key: String): String {
        return this.config.messages[key] ?: "Message $key not found"
    }

    override fun aliases(): List<String> {
        return this.config.aliases
    }

    override fun tablist(): Tablist {
        return this.config.tablist
    }

    override fun motd(): Motd {
        return this.config.motd
    }

    override fun maintenanceMotd(): Motd {
        return this.config.maintenanceMotd
    }
}