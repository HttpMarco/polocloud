package dev.httpmarco.polocloud.addons.proxy

import dev.httpmarco.polocloud.addons.proxy.tablist.Tablist

data class ProxyConfiguration(
    var prefix: String = "",
    val messages: MutableMap<String, String> = mutableMapOf(
        "no_permission" to "§cYou have no permission!",
        "only_players" to "§cOnly players can use this command!"
    ),
    val tablist: Tablist = Tablist()
)
