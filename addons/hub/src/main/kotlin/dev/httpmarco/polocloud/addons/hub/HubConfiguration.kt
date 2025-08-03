package dev.httpmarco.polocloud.addons.hub

data class HubConfiguration (
    var prefix: String = "",
    val messages: MutableMap<String, String> = mutableMapOf(
        "no_permission" to "§cYou have no permission!",
        "only_players" to "§cOnly players can use this command!",
        "no_fallback_found" to "§cNo fallback server found!",
        "connected_to_fallback" to "§aYou have been connected to the fallback server §e{server}§a!",
        "already_connected_to_fallback" to "§cYou are already connected to the fallback server §e{server}§c!",
    ),
    val aliases: List<String> = listOf("lobby", "l"),
)