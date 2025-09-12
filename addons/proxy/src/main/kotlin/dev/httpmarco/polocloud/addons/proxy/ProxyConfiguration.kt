package dev.httpmarco.polocloud.addons.proxy

import dev.httpmarco.polocloud.addons.proxy.motd.Motd
import dev.httpmarco.polocloud.addons.proxy.tablist.Tablist

data class ProxyConfiguration(
    var prefix: String = "",
    val messages: MutableMap<String, String> = mutableMapOf(
        "no_permission" to "§cYou have no permission!",
        "only_players" to "§cOnly players can use this command!",
        "starting" to "§8[§a✔§8] §7Command for §b%service% §7executed.",
        "stopping" to "§8[§a✔§8] §7Command for §b%service% §7executed.",
        "stopped" to "§8[§a✔§8] §7Command for §b%service% §7executed.",
        "no_players_online" to "§7There are no players online.",
        "no_server_found" to "§7No services found.",
        
        // StartSubCommand messages
        "group_not_found" to "§cGroup §b%group% does not exist!",
        
        // StopSubCommand messages
        "service_not_found" to "§cService §b%service% does not exist!",
        
        // PlayersSubCommand messages
        "no_players_online_players" to "§7No players are currently online.",
        "players_header" to "§b§lOnline Players (%count%)",
        "player_server_info" to "§b%player% §7→ §a%server%",
        "players_footer" to "§7Distributed across %serverCount% server(s)",
        
        // ListSubCommand messages
        "services_header" to "§b§lAvailable Services",
        "service_info" to "§b%service% §7(§a%status%§7) §7→ §e%players%§7/§e%maxPlayers%",

        // MaintenanceSubCommand messages
        "maintenance_enabled" to "§7Maintenance mode has been §aenabled.",
        "maintenance_disabled" to "§7Maintenance mode has been §adisabled.",
        "maintenance_enabled_already" to "§7Maintenance mode is already §aenabled.",
        "maintenance_disabled_already" to "§7Maintenance mode is already §adisabled.",
        "maintenance_kick" to "§cMaintenance mode is currently enabled. Please try again later.",

        // General usage messages
        "usage_header" to "§7Available §b§l/polocloud> commands:",
        "usage_info" to "§b/polocloud info",
        "usage_list" to "§b/polocloud list",
        "usage_players" to "§b/polocloud players",
        "usage_stop" to "§b/polocloud stop <service>",
        "usage_start" to "§b/polocloud start <groupName>",
        "usage_create" to "§b/polocloud create <template> [static]",
        "usage_delete" to "§b/polocloud delete <server>",
        "usage_maintenance" to "§b/polocloud maintenance <on|off> [group]",
        "usage_broadcast" to "§b/polocloud broadcast <message>"
    ),
    val aliases: List<String> = listOf("cloud", "p"),
    val tablist: Tablist = Tablist(),
    val motd: Motd = Motd(),
    val maintenanceMotd: Motd = Motd()
)
