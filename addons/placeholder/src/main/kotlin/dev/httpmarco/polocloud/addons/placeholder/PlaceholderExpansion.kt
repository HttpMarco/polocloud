package dev.httpmarco.polocloud.addons.placeholder

import dev.httpmarco.polocloud.sdk.java.Polocloud
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

class PlaceholderExpansion(private val polocloudProvider: () -> Polocloud?) : PlaceholderExpansion() {
    
    override fun getIdentifier(): String {
        return "polocloud"
    }
    
    override fun getAuthor(): String {
        return "PoloCloud"
    }
    
    override fun getVersion(): String {
        return "1.0.0"
    }
    
    override fun onPlaceholderRequest(player: Player?, params: String): String? {
        if (player == null) return null

        val polocloud = polocloudProvider() ?: return null
        val serviceName = System.getenv("service-name") ?: "unknown"
        val service = polocloud.serviceProvider().find(serviceName)
        
        return when (params.lowercase()) {
            "service_name" -> serviceName
            "service_state" -> service?.state?.name ?: "UNKNOWN"
            "service_players" -> service?.playerCount?.toString() ?: "0"
            "service_max_players" -> service?.maxPlayerCount?.toString() ?: "0"
            "service_memory_usage" -> service?.memoryUsage?.toString() ?: "0.0"
            "service_cpu_usage" -> service?.cpuUsage?.toString() ?: "0.0"
            "service_motd" -> service?.motd ?: ""
            "service_hostname" -> service?.hostname ?: "unknown"
            "service_port" -> service?.port?.toString() ?: "0"
            "service_group" -> service?.groupName ?: "unknown"
            "service_id" -> service?.id?.toString() ?: "0"
            "service_uptime" -> {
                if (service != null) {
                    val uptime = System.currentTimeMillis() - service.information.createdAt
                    formatUptime(uptime)
                } else {
                    "Unknown"
                }
            }

            "cloud_services" -> polocloud.serviceProvider().findAll().size.toString()
            "cloud_players" -> polocloud.playerProvider().findAll().size.toString()
            "cloud_groups" -> polocloud.groupProvider().findAll().size.toString()
            "cloud_player_count" -> polocloud.playerProvider().playerCount().toString()

            "player_name" -> player.name
            "player_service" -> {
                val polocloudPlayer = polocloud.playerProvider().findByName(player.name)
                polocloudPlayer?.currentServiceName ?: serviceName
            }

            "group_name" -> service?.groupName ?: "unknown"
            "group_services" -> {
                if (service != null) {
                    polocloud.serviceProvider().findByGroup(service.groupName).size.toString()
                } else {
                    "0"
                }
            }

            else -> null
        }
    }
    
    private fun formatUptime(uptime: Long): String {
        val seconds = uptime / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        
        return when {
            days > 0 -> "${days}d ${hours % 24}h ${minutes % 60}m"
            hours > 0 -> "${hours}h ${minutes % 60}m"
            minutes > 0 -> "${minutes}m ${seconds % 60}s"
            else -> "${seconds}s"
        }
    }
}
