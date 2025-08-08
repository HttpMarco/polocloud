package dev.httpmarco.polocloud.addons.proxy

import dev.httpmarco.polocloud.addons.proxy.tablist.Tablist

data class ProxyConfiguration(
    var prefix: String = "",
    val messages: MutableMap<String, String> = mutableMapOf(
        "no_permission" to "§cYou have no permission!",
        "only_players" to "§cOnly players can use this command!",
        "starting" to "<dark_gray>[</dark_gray><green>✔</green><dark_gray>]</dark_gray> <gray>Command for <aqua>%service%</aqua> executed.</gray>",
        "stopping" to "<dark_gray>[</dark_gray><green>✔</green><dark_gray>]</dark_gray> <gray>Command for <aqua>%service%</aqua> executed.</gray>",
        "stopped" to "<dark_gray>[</dark_gray><green>✔</green><dark_gray>]</dark_gray> <gray>Command for <aqua>%service%</aqua> executed.</gray>",
        "no_players_online" to "<gray>There are no players online.</gray>",
        "no_server_found" to "<gray>No services found.</gray>",
        
        // StartSubCommand messages
        "group_not_found" to "<red>Group <aqua>%group%</aqua> does not exist!</red>",
        "usage_start" to "<gray>Usage: <aqua>/polocloud start <groupName></aqua>",
        
        // StopSubCommand messages
        "service_not_found" to "<red>Service <aqua>%service%</aqua> does not exist!</red>",
        "usage_stop" to "<gray>Usage: <aqua>/polocloud stop <service></aqua>",
        
        // SendSubCommand messages
        "player_not_online" to "<gray>Player <aqua>%player%</aqua> is not online.</gray>",
        "server_not_found" to "<gray>Server <aqua>%server%</aqua> not found.</gray>",
        "player_already_on_server" to "<gray>Player <aqua>%player%</aqua> is already on server <aqua>%server%</aqua>.</gray>",
        "send_success" to "<green>Successfully</green> <gray>sent <aqua>%player%</aqua> to server <aqua>%server%</aqua>.</gray>",
        "usage_send" to "<gray>Usage: <aqua>/polocloud send <player> <server></aqua>",
        
        // KickAllSubCommand messages
        "no_players_on_server" to "<gray>No players found on server <aqua>%server%</aqua>.</gray>",
        "kick_success_server" to "<green>Successfully kicked <aqua>%count%</aqua> player(s) from server <aqua>%server%</aqua>.</green>",
        "kick_success_network" to "<green>Successfully kicked <aqua>%count%</aqua> player(s) from the network.</green>",
        "usage_kickall" to "<gray>Usage: <aqua>/polocloud kickall [server]</aqua>",
        
        // PlayersSubCommand messages
        "no_players_online_players" to "<gray>No players are currently online.</gray>",
        "players_header" to "<gradient:#00fdee:#118bd1><bold>Online Players (%count%)</bold></gradient>",
        "player_server_info" to "<aqua>%player%</aqua> <gray>→</gray> <green>%server%</green>",
        "players_footer" to "<gray>Distributed across %serverCount% server(s)</gray>",
        
        // ListSubCommand messages
        "services_header" to "<gradient:#00fdee:#118bd1><bold>Available Services</bold></gradient>",
        "service_info" to "<aqua>%service%</aqua> <gray>(</gray><green>%status%</green><gray>)</gray> <gray>→</gray> <yellow>%players%</yellow><gray>/</gray><yellow>%maxPlayers%</yellow>",
        
        // General usage messages
        "usage_header" to "<gray>Available <gradient:#00fdee:#118bd1><bold>/polocloud</bold></gradient> commands:",
        "usage_info" to "<aqua>/polocloud info</aqua>",
        "usage_list" to "<aqua>/polocloud list</aqua>",
        "usage_players" to "<aqua>/polocloud players</aqua>",
        "usage_create" to "<aqua>/polocloud create <template> [static]</aqua>",
        "usage_delete" to "<aqua>/polocloud delete <server></aqua>",
        "usage_maintenance" to "<aqua>/polocloud maintenance <on|off> [group]</aqua>",
        "usage_broadcast" to "<aqua>/polocloud broadcast <message></aqua>"
    ),
    val aliases: List<String> = listOf("cloud", "p"),
    val tablist: Tablist = Tablist()
)
