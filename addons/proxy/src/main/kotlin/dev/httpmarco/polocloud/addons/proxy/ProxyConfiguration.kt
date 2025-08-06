package dev.httpmarco.polocloud.addons.proxy

import dev.httpmarco.polocloud.addons.proxy.tablist.Tablist

data class ProxyConfiguration(
    var prefix: String = "",
    val messages: MutableMap<String, String> = mutableMapOf(
        "no_permission" to "§cYou have no permission!",
        "only_players" to "§cOnly players can use this command!",
        "starting" to "<dark_gray>[</dark_gray><green>+</green><dark_gray>]</dark_gray> <gray>Service <aqua>%service%</aqua> starting...</gray>",
        "stopping" to "<dark_gray>[</dark_gray><gold>*</gold><dark_gray>]</dark_gray> <gray>Service <aqua>%service%</aqua> stopping...</gray>",
        "stopped" to "<dark_gray>[</dark_gray><red>-</red><dark_gray>]</dark_gray> <gray>Service <aqua>%service%</aqua> stopped.</gray>",
    ),
    val aliases: List<String> = listOf("cloud", "p"),
    val tablist: Tablist = Tablist()
)
