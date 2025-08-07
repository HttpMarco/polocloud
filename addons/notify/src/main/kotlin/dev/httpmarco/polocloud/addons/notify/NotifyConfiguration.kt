package dev.httpmarco.polocloud.addons.notify

data class NotifyConfiguration(
    var prefix: String = "",
    val messages: MutableMap<String, String> = mutableMapOf(
        "no_permission" to "§cYou have no permission!",
        "only_players" to "§cOnly players can use this command!",
        "service_starting" to "§7Service §b%service% §7is starting! §8[§6⌛§8]",
        "service_online" to "§7Service §b%service% §7is now online! §8[§a✔§8]",
        "service_shutdown" to "§7Service §b%service% §7has been shut down. §8[§c✘§8]",
    )
)
